package com.slibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.slibrary.model.User;
import com.slibrary.util.DBConnection;

public class UserDAO {

	// user_id를 통해 해당 유저 정보를 가져오는 메소드
	public User getUser(int id) throws Exception {
		String sql = "SELECT * FROM users WHERE id = ? AND deleted_at IS NULL";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"),
						rs.getDate("created_at"), rs.getDate("update_at"), rs.getDate("deleted_at"));
			}
		}
		return null;

	}

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

}
