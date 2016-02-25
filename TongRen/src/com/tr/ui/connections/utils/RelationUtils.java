package com.tr.ui.connections.utils;

import java.util.ArrayList;

import com.tr.model.model.PeopleAddress;
import com.tr.model.model.PeopleCommunityRelationship;
import com.tr.model.model.PeopleContactComm;
import com.tr.model.model.PeopleCustomer;
import com.tr.model.model.PeopleGroupTemp;
import com.tr.model.model.PeopleImportantDate;
import com.tr.model.model.PeopleMetting;
import com.tr.model.model.PeopleName;
import com.tr.model.model.PeoplePersonalLine;
import com.tr.model.model.PeopleSelectTag;
import com.tr.model.model.PeopleTemp;
import com.tr.model.obj.JTContact2;

public class RelationUtils {
	/**
	 * 填充模版中的基本信息 
	 * @param jtContactTemplate
	 */
	public static void fillBasicInfomation2Template(JTContact2 jtContactTemplate) {

		if (jtContactTemplate.getMyPeopleGroupTempList() == null) {
			jtContactTemplate.setMyPeopleGroupTempList(new ArrayList<PeopleGroupTemp>());
		}
		if (jtContactTemplate.getMyPeopleCustomerList() == null) {
			jtContactTemplate.setMyPeopleCustomerList(new ArrayList<PeopleCustomer>());
		}
		if (jtContactTemplate.getMyPeopleSelectTagList() == null) {
			jtContactTemplate.setMyPeopleSelectTagList(new ArrayList<PeopleSelectTag>());
		}
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.peopleNameList == null) {
			peopleTemp.peopleNameList = (new ArrayList<PeopleName>());
		}
		if (peopleTemp.peopleNameList.size() == 0) {
			PeopleName peopleName = new PeopleName();
			PeopleSelectTag peopleSelectTag = new PeopleSelectTag(0, "1", "中文名");
			peopleName.name = ("");
			peopleName.typeTag = (peopleSelectTag);
			peopleTemp.peopleNameList.add(peopleName);
		}
		if (peopleTemp == null) {
			Integer gender = Integer.valueOf(3);
			peopleTemp.gender = (gender);
		}
		if (peopleTemp.peopleGroupList == null) {
			peopleTemp.peopleGroupList=(new ArrayList<PeopleGroupTemp>());
		}
		if (peopleTemp.peopleCustomerList == null) {
			peopleTemp.peopleCustomerList = (new ArrayList<PeopleCustomer>());
		}
	}

	/**
	 * 填充模版中的联系方式
	 * @param jtContactTemplate
	 */
	public static void fillContactInfomation2Template(JTContact2 jtContactTemplate) {
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.contactMobileList == null) { // 手机
			peopleTemp.contactMobileList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactMobileList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content = ("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "手机");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactMobileList.add(contactComm);
		}
		if (peopleTemp.contactFixedList == null) { // 固话
			peopleTemp.contactFixedList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactFixedList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content = ("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "固话");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactFixedList.add(contactComm);
		}
		if (peopleTemp.contactFixedList == null) { // 传真
			peopleTemp.contactFixedList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactFixedList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content = ("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "住宅传真");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactFixedList.add(contactComm);
		}
		if (peopleTemp.contactMailList == null) { // 邮箱类型
			ArrayList<PeopleContactComm> mailList = new ArrayList<PeopleContactComm>();
			peopleTemp.contactMailList = (mailList);
		}
		if (peopleTemp.contactMailList.size() == 0) {
			for (int i = 0; i < 1; i++) {
				PeopleContactComm contactComm = new PeopleContactComm();
				contactComm.content = ("");
				PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "主要邮箱");
				contactComm.typeTag = (typeTag);
				peopleTemp.contactMailList.add(contactComm);
			}
		}
		if (peopleTemp.contactHomeList == null) { // 主页类型
			peopleTemp.contactHomeList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactHomeList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content = ("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "个人主页");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactHomeList.add(contactComm);
		}
		if (peopleTemp.contactCommunicationList == null) { // 通讯类型
			peopleTemp.contactCommunicationList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactCommunicationList.size() == 0) {
			for (int i = 0; i < 1; i++) {
				PeopleContactComm contactComm = new PeopleContactComm();
				contactComm.content=("");
				PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "QQ");
				contactComm.typeTag = (typeTag);
				peopleTemp.contactCommunicationList.add(contactComm);
			}
		}
		if (peopleTemp.contactAddressList == null) { // 地址
			peopleTemp.contactAddressList = (new ArrayList<PeopleAddress>());
		}
		if (peopleTemp.contactAddressList.size() == 0) {
			PeopleAddress address = new PeopleAddress();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "住宅地址");
			address.typeTag = (typeTag);
			address.areaType = (0);
			address.stateName = ("");
			address.cityName = ("");
			address.countyName = ("");
			peopleTemp.contactAddressList.add(address);
		}
		if (peopleTemp.personalLineList == null) { // 自定义字段
			peopleTemp.personalLineList = (new ArrayList<PeoplePersonalLine>());
		}
	}

	/**
	 * 填充模版中的个人情况
	 * @param jtContactTemplate
	 */
	public static void fillPersonalInfomation2Template(JTContact2 jtContactTemplate) {
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.raceName == null){
			peopleTemp.raceName = ("");
		}
		if (peopleTemp.nationalityName == null){
			peopleTemp.nationalityName = ("");
		}
		if (peopleTemp.birthPlaceCountryName == null){
			peopleTemp.birthPlaceCountryName = ("");
		}
		if (peopleTemp.birthPlaceStateName == null){
			peopleTemp.birthPlaceStateName = ("");
		}
		if (peopleTemp.birthPlaceCityName == null){
			peopleTemp.birthPlaceCityName = ("");
		}
		if (peopleTemp.birthPlaceCountyName == null){
			peopleTemp.birthPlaceCountyName = ("");
		}
		if (peopleTemp.faithName == null){
			peopleTemp.faithName = ("");
		}
		if (peopleTemp.bodySituation == null){
			peopleTemp.bodySituation = ("");
		}
		if (peopleTemp.hobby == null){
			peopleTemp.hobby = ("");
		}
		if (peopleTemp.habit == null){
			peopleTemp.habit=("");
		}
		// 重要日期
		if (peopleTemp.importantDateList == null) {
			peopleTemp.importantDateList=(new ArrayList<PeopleImportantDate>());
		}
		if (peopleTemp.importantDateList.size() == 0) {
			PeopleImportantDate peopleImportantDate = new PeopleImportantDate();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "生日");
			peopleImportantDate.typeTag = (typeTag);
			peopleImportantDate.date=("");
			peopleTemp.importantDateList.add(peopleImportantDate);
		}
		if (peopleTemp.communityRelationshipList == null) {
			peopleTemp.communityRelationshipList = (new ArrayList<PeopleCommunityRelationship>());
		}
		if (peopleTemp.communityRelationshipList.size() == 0) {
			PeopleCommunityRelationship communityRelationship = new PeopleCommunityRelationship();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "配偶");
			communityRelationship.typeTag = (typeTag);
			communityRelationship.content = ("");
			peopleTemp.communityRelationshipList.add(communityRelationship);
		}
		// 自定义字段
		if (peopleTemp.situationPersonalLineList == null) {
			peopleTemp.situationPersonalLineList = (new ArrayList<PeoplePersonalLine>());
		}
	}

	/**
	 * 填充模版中的会面情况
	 * @param jtContactTemplate
	 */
	public static void fillMeeting2Template(JTContact2 jtContactTemplate) {

		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.meetingList == null) {
			ArrayList<PeopleMetting> peopleMettingList = new ArrayList<PeopleMetting>();
			PeopleMetting peopleMetting = new PeopleMetting();
			peopleMettingList.add(peopleMetting);
			peopleTemp.meetingList = (peopleMettingList);
		}
		PeopleMetting peopleMetting = peopleTemp.meetingList.get(0);
		if (peopleMetting.areaType == null){ // 地区类型：0-国内，1-国外
			peopleMetting.areaType = (0);
		}
		if (peopleMetting.personalLineList == null) { // 自定义字段
			ArrayList<PeoplePersonalLine> customList = new ArrayList<PeoplePersonalLine>();
			peopleMetting.personalLineList = (customList);
		}
	}
}
