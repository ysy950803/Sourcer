package com.ysy.student.entity;

import java.io.Serializable;

public class Student implements Serializable{

	private long id;
	private String name;
	private String apartment;
	private String sex;
	private String classes;
	private String phoneNumber;
	private String birthDay;
	private String modifyDateTime;
	
	
	public Student() {
		super();
	}

	public Student(long id, String name, String apartment, String sex, String classes, String phoneNumber,
			String birthDay, String modifyDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.apartment = apartment;
		this.sex = sex;
		this.classes = classes;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
		this.modifyDateTime = modifyDateTime;
	}
	
	public Student(String name, String apartment, String sex, String classes, String phoneNumber,
			String birthDay, String modifyDateTime) {
		super();
		this.name = name;
		this.apartment = apartment;
		this.sex = sex;
		this.classes = classes;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
		this.modifyDateTime = modifyDateTime;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setTrainDate(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getModifyDateTime() {
		return modifyDateTime;
	}
	public void setModifyDateTime(String modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartment == null) ? 0 : apartment.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((classes == null) ? 0 : classes.hashCode());
		result = prime * result + ((modifyDateTime == null) ? 0 : modifyDateTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((birthDay == null) ? 0 : birthDay.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (apartment == null) {
			if (other.apartment != null)
				return false;
		} else if (!apartment.equals(other.apartment))
			return false;
		if (id != other.id)
			return false;
		if (classes == null) {
			if (other.classes != null)
				return false;
		} else if (!classes.equals(other.classes))
			return false;
		if (modifyDateTime == null) {
			if (other.modifyDateTime != null)
				return false;
		} else if (!modifyDateTime.equals(other.modifyDateTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (birthDay == null) {
			if (other.birthDay != null)
				return false;
		} else if (!birthDay.equals(other.birthDay))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + "]";
	}

}
