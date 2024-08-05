package com.slibrary.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
	private int id;
	private String name, email, password;
	private Date created_at, updated_at, deleted_at;
}
