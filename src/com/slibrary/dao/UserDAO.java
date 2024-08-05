package com.slibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

}
