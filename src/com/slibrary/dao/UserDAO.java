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

}
