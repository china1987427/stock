package com.china.stock.common.tool.entity;

/**
 * 用户信息实体类
 */
public class UserEntity {



	public UserEntity() {

	}

	public UserEntity(Integer sa_ID, String sa_LoginName, String sa_Mobile,
			String sa_Email, Long ma_ID, Integer mb_Country,
			Integer mb_Province, Integer mb_City, Integer mb_County,
			String mb_Name, String mb_PetName, Long ms_ID,String mb_ResidentialAddress,String mb_tel) {
		super();
		this.sa_ID = sa_ID;
		this.sa_LoginName = sa_LoginName;
		this.sa_Mobile = sa_Mobile;
		this.sa_Email = sa_Email;
		this.ma_ID = ma_ID;
		this.mb_Country = mb_Country;
		this.mb_Province = mb_Province;
		this.mb_City = mb_City;
		this.mb_County = mb_County;
		this.mb_Name = mb_Name;
		this.mb_PetName = mb_PetName;
		this.ms_ID = ms_ID;
		this.mb_ResidentialAddress=mb_ResidentialAddress;
		this.mb_tel=mb_tel;
	}

	private String token;

	// 统一认证中的信息
	private Integer sa_ID; // 统一登录帐号ID
	private String sa_LoginName; // 统一登录名
	private String sa_Mobile;// 手机号码
	private String sa_Email;// 电子邮箱

	// 完善信息
	private Long ma_ID; // 本地帐号ID
	private Integer mb_Country; // 国家
	private Integer mb_Province; // 省/直辖市
	private Integer mb_City; // 市
	private Integer mb_County; // 区/县
	private String mb_Name; // 姓名
	private String mb_PetName; // 昵称
	private String mb_ResidentialAddress; //联系地址
	private String mb_tel; // 座机电话
	// 店铺信息
	private Long ms_ID; // 店铺ID

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSa_ID() {
		return sa_ID;
	}

	public void setSa_ID(Integer sa_ID) {
		this.sa_ID = sa_ID;
	}

	public String getSa_LoginName() {
		return sa_LoginName;
	}

	public void setSa_LoginName(String sa_LoginName) {
		this.sa_LoginName = sa_LoginName;
	}

	public String getSa_Mobile() {
		return sa_Mobile;
	}

	public void setSa_Mobile(String sa_Mobile) {
		this.sa_Mobile = sa_Mobile;
	}

	public String getSa_Email() {
		return sa_Email;
	}

	public void setSa_Email(String sa_Email) {
		this.sa_Email = sa_Email;
	}

	public Long getMa_ID() {
		return ma_ID;
	}

	public void setMa_ID(Long ma_ID) {
		this.ma_ID = ma_ID;
	}

	public Integer getMb_Country() {
		return mb_Country;
	}

	public void setMb_Country(Integer mb_Country) {
		this.mb_Country = mb_Country;
	}

	public Integer getMb_Province() {
		return mb_Province;
	}

	public void setMb_Province(Integer mb_Province) {
		this.mb_Province = mb_Province;
	}

	public Integer getMb_City() {
		return mb_City;
	}

	public void setMb_City(Integer mb_City) {
		this.mb_City = mb_City;
	}

	public Integer getMb_County() {
		return mb_County;
	}

	public void setMb_County(Integer mb_County) {
		this.mb_County = mb_County;
	}

	public String getMb_Name() {
		return mb_Name;
	}

	public void setMb_Name(String mb_Name) {
		this.mb_Name = mb_Name;
	}

	public String getMb_PetName() {
		return mb_PetName;
	}

	public void setMb_PetName(String mb_PetName) {
		this.mb_PetName = mb_PetName;
	}

	public Long getMs_ID() {
		return ms_ID;
	}

	public void setMs_ID(Long ms_ID) {
		this.ms_ID = ms_ID;
	}

	public String getMb_ResidentialAddress() {
		return mb_ResidentialAddress;
	}

	public void setMb_ResidentialAddress(String mb_ResidentialAddress) {
		this.mb_ResidentialAddress = mb_ResidentialAddress;
	}

	public String getMb_tel() {
		return mb_tel;
	}

	public void setMb_tel(String mb_tel) {
		this.mb_tel = mb_tel;
	}
	
}
