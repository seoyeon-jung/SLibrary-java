package com.slibrary.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.slibrary.service.BookService;
import com.slibrary.service.UserService;
import com.slibrary.util.DBConnection;

public class SLibraryExample {
	public static Scanner sc = new Scanner(System.in); // 입력 받는 scanner

	// menu 출력을 위한 boolean
	public static boolean isActive = true;
	public static boolean isUserMenuActive = true;
	public static boolean isMainMenuActive = true;

	// user, book, loan 인스턴스 생성
	public static UserService us = new UserService();
	public static BookService book = new BookService(us);

	public static void main(String[] args) {
		// database 연결
		try (Connection conn = DBConnection.getConnection()) {

			while (isActive) {
				System.out.println("\n ========== LOG IN PLEASE ==========");
				System.out.println("1. 회원가입 | 2. 로그인");

				int num = sc.nextInt();
				sc.nextLine();

				switch (num) {
				case 1:
					us.signUpUser(); // 회원가입
					mainMenu();
					break;
				case 2:
					us.loginUser(); // 로그인
					mainMenu();
					break;
				default:
					isActive = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sc != null)
				sc.close();
		}

	}

	// 로그인 이후의 main menu
	private static void mainMenu() throws Exception {
		while (isMainMenuActive) {
			System.out.println("\n ========== MAIN PAGE ==========");
			System.out.println("1. 대출 가능한 책 목록 | 2. 책 대출하기 | 3. 책 반납하기 | 4. 마이 페이지 | 5. 로그아웃");

			int num = sc.nextInt();
			sc.nextLine();

			switch (num) {
			case 1:
				book.printBookList(); // 대출가능한 책 목록
				break;
			case 2:
				book.borrowBook(); // 책 대출하기
				break;
			case 3:
				System.out.println("책 반납하기");
				break;
			case 4:
				System.out.println("마이 페이지 메뉴 들어가기");
				userMenu();
				break;
			case 5:
			default:
				System.out.println("로그아웃, 다시 메인으로 돌아가기");
				isMainMenuActive = false;
				break;
			}
		}
	}

	// my page
	private static void userMenu() throws Exception {

		while (isUserMenuActive) {
			System.out.println("\n ========== USER PAGE ==========");
			System.out.println("1. 비밀번호 변경 | 2. 내가 빌린 책 목록 | 3. 이전으로 | 4. 로그아웃 | 5. 탈퇴하기");

			int num = sc.nextInt();
			sc.nextLine();

			switch (num) {
			case 1:
				us.changePassword(); // 비밀번호 변경
				break;
			case 2:
				System.out.println("내가 빌린 책 목록");
				break;
			case 3:
				System.out.println("메인 페이지로 돌아가기");
				isUserMenuActive = false;
				break;
			case 4:
				System.out.println("로그아웃, 다시 메인으로 돌아가기");
				isUserMenuActive = false;
				isMainMenuActive = false;
				break;
			case 5:
				us.deleteUser(); // 회원 탈퇴
				isUserMenuActive = false;
				isMainMenuActive = false;
				break;
			}
		}
	}

}
