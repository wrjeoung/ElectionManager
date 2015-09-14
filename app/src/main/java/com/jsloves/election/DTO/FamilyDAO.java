package com.jsloves.election.DTO;

import java.io.Serializable;

public class FamilyDAO implements Serializable{
	
	//`ADM_CD`,`FAMILY_ONE`,`FAMILY_TWO_OVER`,`MYHOME_RATIO`,`APT_RATIO`,`40M_OVER`
	private static final long serialVersionUID = 1L;
	private int level;
	private String adm_cd; 
	private float family_one;
	private float family_two_over;
	private float myhome_ratio;
	private float apt_ratio;
	private float f40m_over;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public float getFamily_one() {
		return family_one;
	}
	public void setFamily_one(float family_one) {
		this.family_one = family_one;
	}
	public float getFamily_two_over() {
		return family_two_over;
	}
	public void setFamily_two_over(float family_two_over) {
		this.family_two_over = family_two_over;
	}
	public float getMyhome_ratio() {
		return myhome_ratio;
	}
	public void setMyhome_ratio(float myhome_ratio) {
		this.myhome_ratio = myhome_ratio;
	}
	public float getApt_ratio() {
		return apt_ratio;
	}
	public void setApt_ratio(float apt_ratio) {
		this.apt_ratio = apt_ratio;
	}
	public float getF40m_over() {
		return f40m_over;
	}
	public void setF40m_over(float f40m_over) {
		this.f40m_over = f40m_over;
	}
	public String getAdm_cd() {
		return adm_cd;
	}
	public void setAdm_cd(String adm_cd) {
		this.adm_cd = adm_cd;
	}
}
