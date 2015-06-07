package com.example.jma.ar7carshop.Database;

public class CityDB {

	private String nameAr,nameEng;
	private double longitude,latitude;
	
	public String getNameAr(){
		return nameAr;
	}
	public void setNameAr(String nameAr){
		this.nameAr=nameAr;
	}
	public CityDB() {
	}
	public String getNameEng() {
		return nameEng;
	}
	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
