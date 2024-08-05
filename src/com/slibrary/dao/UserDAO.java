package com.slibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.slibrary.model.Book;
import com.slibrary.model.User;
import com.slibrary.util.DBConnection;

public class UserDAO {

	// 회원 가입 (유저 정보를 DB에 추가)
	public int addUser(User newUser) throws Exception {
		String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newUser.getName());
			pstmt.setString(2, newUser.getEmail());
			pstmt.setString(3, newUser.getPassword());

			return pstmt.executeUpdate();
		}
	}

	// 로그인 (DB의 email, password와 비교해서 로그인하기)
	public User login(String email, String password) throws Exception {
		String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"),
						rs.getDate("created_at"), rs.getDate("updated_at"), rs.getDate("deleted_at"));
			}
		}
		return null;
	}

	// 비밀번호 update (password 입력해서 해당 user 맞는지 확인 후에 비밀번호 업데이트)
	public boolean updatePassword(String email, String originPassword, String newPassword) throws Exception {
		// 1. 해당 유저의 비밀번호 체크
		String checkSQL = "SELECT * FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
			pstmt.setString(1, email);
			pstmt.setString(2, originPassword);
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return false; // 비밀번호 일치하지 않음
			}
		}

		// 2. 비밀번호 변경
		String updateSQL = "UPDATE users SET password = ? WHERE email = ? AND deleted_at IS NULL";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			pstmt.setString(1, newPassword);
			pstmt.setString(2, email);

			int resultRow = pstmt.executeUpdate();
			return resultRow > 0;
		}
	}

	// 회원 탈퇴 (deleted_at update하기)
	public boolean deleteUser(String email, String password) throws Exception {
		// 1. 로그인한 유저 확인
		String checkSQL = "SELECT * FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return false; // 유저 정보가 맞지 않음
			}
		}

		// 2. 회원 탈퇴
		String deleteSQL = "UPDATE users SET deleted_at = NOW() WHERE email = ? AND deleted_at IS NULL";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
			pstmt.setString(1, email);

			int resultRow = pstmt.executeUpdate();
			return resultRow > 0;
		}
	}

	// 로그인한 유저가 빌린 책 목록 출력
	public List<Book> getBorrowedBooks(int id) throws Exception {
		String sql = "SELECT b.id, b.isbn, b.title, b.author, b.category, b.available " + "FROM book b "
				+ "JOIN loan l ON b.id = l.book_id " + "WHERE l.member_id = ? AND l.return_date > NOW()";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			List<Book> borrowedBooks = new ArrayList<>();
			while (rs.next()) {
				Book book = new Book(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("author"), rs.getString("category"), rs.getBoolean("available"));
				borrowedBooks.add(book);
			}
			return borrowedBooks;
		}
	}

}
