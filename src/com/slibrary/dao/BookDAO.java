package com.slibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.slibrary.model.Book;
import com.slibrary.util.DBConnection;

public class BookDAO {

	// 대출 가능한 책 목록 가져오기 (avilable이 true인 책들만 가져오기)
	public List<Book> getBookList() throws Exception {
		String sql = "SELECT * FROM book WHERE available = true";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();) {
			List<Book> bookList = new ArrayList<>();

			while (rs.next()) {
				Book book = new Book(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("author"), rs.getString("category"), rs.getBoolean("available"));

				bookList.add(book);
			}
			return bookList;
		}
	}

	// 책 대출하기
	public boolean borrowBook(int bookId, int userId) throws Exception {
		// 1. 책의 대출 상태 확인
		String checkSQL = "SELECT available FROM book WHERE id = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
			pstmt.setInt(1, bookId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getBoolean("available")) {
				// 2. 책의 대출 상태 변경
				String updateBookSQL = "UPDATE book SET available = false WHERE id = ?";
				try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookSQL)) {
					pstmtUpdate.setInt(1, bookId);
					int result = pstmtUpdate.executeUpdate();

					if (result > 0) {
						// 3. 대출 기록을 loan 테이블에 추가
						String insertLoanSQL = "INSERT INTO loan (member_id, book_id, loan_date, return_date) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY))";
						try (PreparedStatement pstmtInsertLoan = conn.prepareStatement(insertLoanSQL)) {
							pstmtInsertLoan.setInt(1, userId);
							pstmtInsertLoan.setInt(2, bookId);
							pstmtInsertLoan.executeUpdate();
						}
						return true;
					}
				}
			}
			return false;
		}
	}

	// 책 반납하기
	public boolean returnBook(int bookId, int userId) throws Exception {
		try (Connection conn = DBConnection.getConnection()) {
			// 1. loan table에서 avilable을 다시 true로 변경하기
			String getLoanInfoSQL = "UPDATE loan SET available = true, return_date = NOW() WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
			try (PreparedStatement pstmtGetLoanInfo = conn.prepareStatement(getLoanInfoSQL)) {
				pstmtGetLoanInfo.setInt(1, bookId);
				pstmtGetLoanInfo.setInt(2, userId);
				ResultSet rs = pstmtGetLoanInfo.executeQuery();

				if (rs.next()) {
					Date loanDate = rs.getDate("loan_date");
					Date today = new Date(System.currentTimeMillis());
					long daysLate = (today.getTime() - loanDate.getTime()) / (1000 * 60 * 60 * 24);

					// 2. 반납 기한 초과 여부 확인
					boolean isLate = daysLate > 14;

					// 3. 대출 기록 업데이트
					String updateLoanSQL = "UPDATE loan SET available = true, return_date = NOW() WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
					try (PreparedStatement pstmtUpdateLoan = conn.prepareStatement(updateLoanSQL)) {
						pstmtUpdateLoan.setInt(1, bookId);
						pstmtUpdateLoan.setInt(2, userId);
						int result = pstmtUpdateLoan.executeUpdate();

						if (result > 0) {
							// 4. 책의 대출 상태 업데이트
							String updateBookSQL = "UPDATE book SET available = true WHERE id = ?";
							try (PreparedStatement pstmtUpdateBook = conn.prepareStatement(updateBookSQL)) {
								pstmtUpdateBook.setInt(1, bookId);
								pstmtUpdateBook.executeUpdate();
							}

							// 5. 반납 상태에 따라 메시지 출력
							if (isLate) {
								System.out.println(daysLate + "일 이후에 반납했습니다.");
							} else {
								System.out.println("반납을 완료했습니다.");
							}

							return true;
						}
					}
				}
			}
		}
		return false;

	}

	// 책 검색하기 (제목 일부로도 검색 가능)
	public List<Book> searchBook(String title) throws Exception {
		String sql = "SELECT * FROM book WHERE title LIKE ? AND available = true";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + title + "%");
			ResultSet rs = pstmt.executeQuery();

			List<Book> bookList = new ArrayList<>();

			while (rs.next()) {
				Book book = new Book(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("author"), rs.getString("category"), rs.getBoolean("available"));

				bookList.add(book);
			}
			return bookList;
		}
	}

}
