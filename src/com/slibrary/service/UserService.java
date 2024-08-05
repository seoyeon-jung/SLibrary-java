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

	// user 정보 가져오기
	private User getUser(int id) throws Exception {
		User user = userDAO.getUser(id);
		return user;
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
			System.out.println("회원가입을 완료하였습니다!");
		} else {
			System.out.println("회원가입에 실패했습니다, 다시 시도해주세요.");
		}
	}
}
