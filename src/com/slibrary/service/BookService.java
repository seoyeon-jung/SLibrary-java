package com.slibrary.service;

import java.util.List;

import com.slibrary.controller.SLibraryExample;
import com.slibrary.dao.BookDAO;
import com.slibrary.model.Book;

public class BookService {
	private BookDAO bookDAO = new BookDAO();
	private UserService us;

	public BookService(UserService us) {
		this.us = us;
	}

	// console에서 값을 입력받는 method
	private String getInput(String name, boolean isRequire) {
		System.out.print(name + " 입력 : ");
		String input = SLibraryExample.sc.nextLine();

		if (input.equals("") && isRequire == false) {
			input = null;
		} else {
			while (input.equals("") && isRequire == true) {
				input = getInput(name, isRequire);
			}
		}

		return input;
	}

	// 현재 대출 가능한 책 목록 불러오기
	public void printBookList() throws Exception {
		System.out.println("\n ========== 현재 대출 가능한 책 목록 ==========");

		// DB에서 책 목록 가져오기
		List<Book> bookList = bookDAO.getBookList();

		// list 출력
		System.out.println("ID \t 제목 \t 작가 \t 분류");
		for (Book book : bookList) {
			System.out.println(
					book.getId() + "\t" + book.getTitle() + "\t" + book.getAuthor() + "\t" + book.getCategory());
		}
	}

	// 책 대출하기
	public void borrowBook() throws Exception {
		if (us.getLoggedInUser() == null) {
			System.out.println("로그인 후에 책을 대출할 수 있습니다.");
			return;
		}

		System.out.println("\n ========== 책 대출하기 ==========");
		printBookList(); // 우선 대출 가능한 책 목록 출력

		int bookId = Integer.parseInt(getInput("책 ID", true));
		int userId = us.getLoggedInUser().getId();

		boolean success = bookDAO.borrowBook(bookId, userId);
		if (success) {
			System.out.println("책을 성공적으로 대출하였습니다.");
		} else {
			System.out.println("책을 대출할 수 없습니다. 다시 한번 시도해주세요.");
		}
	}

	// 책 반납하기
	public void returnBook() throws Exception {
		if (us.getLoggedInUser() == null) {
			System.out.println("로그인 후에 책을 반납할 수 있습니다.");
			return;
		}

		System.out.println("\n ========== 책 반납하기 ==========");

		us.printBorrowedBook(); // 우선 현재 user가 반납 가능한 책 목록 출력
		int bookId = Integer.parseInt(getInput("책 ID", true));
		int userId = us.getLoggedInUser().getId();

		boolean success = bookDAO.returnBook(bookId, userId);
		if (success) {
			System.out.println("책을 성공적으로 반납하였습니다.");
		} else {
			System.out.println("책 반납에 실패했습니다. 다시 한번 시도해주세요.");
		}
	}

	// 책 검색하기
	public void searchBook() throws Exception {
		System.out.println("\n ========== 책 제목으로 검색하기 ==========");

		String title = getInput("책 제목", true);

		// DB에서 책 목록 가져오기
		List<Book> bookList = bookDAO.searchBook(title);

		// list 출력
		if (bookList.isEmpty()) {
			System.out.println("검색 결과가 없습니다.");
		} else {
			System.out.println("ID \t 제목 \t 작가 \t 분류");
			for (Book book : bookList) {
				System.out.println(
						book.getId() + "\t" + book.getTitle() + "\t" + book.getAuthor() + "\t" + book.getCategory());
			}
		}

	}
}
