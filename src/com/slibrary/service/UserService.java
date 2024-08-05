package com.slibrary.service;

import com.slibrary.controller.SLibraryExample;
import com.slibrary.dao.UserDAO;
import com.slibrary.model.User;

public class UserService {
	// userDAO 인스턴스 생성
	private UserDAO userDAO = new UserDAO();

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

	// 회원 가입
	public void signUpUser() throws Exception {
		System.out.println("\n ========== 회원가입 ==========");
		String name = getInput("이름", true);
		String email = getInput("이메일", true);
		String password = getInput("비밀번호", true);

		// DB에 저장
		User newUser = new User(0, name, email, password, null, null, null);
		int result = userDAO.addUser(newUser);
		if (result > 0) {
			System.out.println("\n" + newUser.getName() + "님 안녕하세요!");
		} else {
			System.out.println("회원가입에 실패했습니다, 다시 시도해주세요.");
		}
	}

	// 로그인
	public void loginUser() throws Exception {
		System.out.println("\n ========== 로그인 ==========");
		String email = getInput("이메일", true);
		String password = getInput("비밀번호", true);

		// DB에 저장
		User user = userDAO.login(email, password);
		if (user != null) {
			System.out.println("\n" + user.getName() + "님 안녕하세요!");
		} else {
			System.out.println("로그인에 실패했습니다. 다시 시도해주세요.");
		}
	}

	// 비밀번호 변경
	public void changePassword() throws Exception {
		System.out.println("\n ========== 비밀번호 변경 ==========");

		// 비밀번호를 입력한 뒤에, 해당 user가 맞는지 다시 한 번 체크한 뒤에 비밀번호 변경하기
		String email = getInput("이메일", true);
		String originPassword = getInput("현재 비밀번호", true);
		String newPassword = getInput("새로운 비밀번호", true);

		// DB에 저장
		boolean success = userDAO.updatePassword(email, originPassword, newPassword);
		if (success) {
			System.out.println("비밀번호가 성공적으로 변경되었습니다.");
		} else {
			System.out.println("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
		}
	}

	// 회원 탈퇴
	public void deleteUser() throws Exception {
		System.out.println("\n ========== 회원 탈퇴 ==========");

		// 로그인했음을 알리기 위해 다시 이메일과 비밀번호 입력한 후에 탈퇴해야 한다.
		String email = getInput("이메일", true);
		String password = getInput("비밀번호", true);

		// DB에서 deleted_at update
		boolean isDeleted = userDAO.deleteUser(email, password);
		if (isDeleted) {
			System.out.println("회원 탈퇴가 성공적으로 완료되었습니다.");
		} else {
			System.out.println("회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
		}
	}

}
