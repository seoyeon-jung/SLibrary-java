package com.slibrary.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Loan {
	private int id, member_id, book_id;
	private boolean available;
	private Date loan_date, return_date;
}
