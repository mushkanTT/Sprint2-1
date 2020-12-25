package com.cg.bugtracking.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity //This annotation is used to create table
@Table(name="admin_table")//This annotation is used to declare the table name
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="admin_id",length=10)
	private int adminId;
	@NotEmpty(message = "Admin name must not be empty")
	@Column(name="admin_name")
	private String adminName;
	@NotEmpty(message = "Admin contact must not be empty")
	@Column(name="admin_contact",length=10)
	private String adminContact;
	@Column(name="admin_userId",length=10)
	private String adminUserid;
	@Column(name="admin_password")
	@NotEmpty(message="Please provide valid adminPassword")
	private String adminPassword;
	public Admin() {
		super();
	}
	public Admin(int adminId, String adminName, String adminContact, String adminUserid, String adminPassword) {
		super();
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminContact = adminContact;
		this.adminUserid = adminUserid;
		this.adminPassword = adminPassword;
	}
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminContact() {
		return adminContact;
	}
	public void setAdminContact(String adminContact) {
		this.adminContact = adminContact;
	}
	public String getAdminUserid() {
		return adminUserid;
	}
	public void setAdminUserid(String adminUserid) {
		this.adminUserid = adminUserid;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	
	
	
	
	
}
