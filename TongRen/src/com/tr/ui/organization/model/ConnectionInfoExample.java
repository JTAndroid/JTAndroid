package com.tr.ui.organization.model;

import java.util.ArrayList;
import java.util.List;


public class ConnectionInfoExample {
    public String orderByClause;

    public boolean distinct;

    public List<Criteria> oredCriteria;

    public int limitStart = -1;

    public int limitEnd = -1;

    public ConnectionInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

  

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    public Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

 

    public abstract static class GeneratedCriteria {
        public List<Criterion> criteria;

        public GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        public void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        public void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidIsNull() {
            addCriterion("customerId is null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidIsNotNull() {
            addCriterion("customerId is not null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidEqualTo(Long value) {
            addCriterion("customerId =", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotEqualTo(Long value) {
            addCriterion("customerId <>", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidGreaterThan(Long value) {
            addCriterion("customerId >", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidGreaterThanOrEqualTo(Long value) {
            addCriterion("customerId >=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidLessThan(Long value) {
            addCriterion("customerId <", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidLessThanOrEqualTo(Long value) {
            addCriterion("customerId <=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidIn(List<Long> values) {
            addCriterion("customerId in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotIn(List<Long> values) {
            addCriterion("customerId not in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidBetween(Long value1, Long value2) {
            addCriterion("customerId between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotBetween(Long value1, Long value2) {
            addCriterion("customerId not between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andTagIsNull() {
            addCriterion("tag is null");
            return (Criteria) this;
        }

        public Criteria andTagIsNotNull() {
            addCriterion("tag is not null");
            return (Criteria) this;
        }

        public Criteria andTagEqualTo(String value) {
            addCriterion("tag =", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotEqualTo(String value) {
            addCriterion("tag <>", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThan(String value) {
            addCriterion("tag >", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThanOrEqualTo(String value) {
            addCriterion("tag >=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThan(String value) {
            addCriterion("tag <", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThanOrEqualTo(String value) {
            addCriterion("tag <=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLike(String value) {
            addCriterion("tag like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotLike(String value) {
            addCriterion("tag not like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagIn(List<String> values) {
            addCriterion("tag in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotIn(List<String> values) {
            addCriterion("tag not in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagBetween(String value1, String value2) {
            addCriterion("tag between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotBetween(String value1, String value2) {
            addCriterion("tag not between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andConntypeIsNull() {
            addCriterion("connType is null");
            return (Criteria) this;
        }

        public Criteria andConntypeIsNotNull() {
            addCriterion("connType is not null");
            return (Criteria) this;
        }

        public Criteria andConntypeEqualTo(Integer value) {
            addCriterion("connType =", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeNotEqualTo(Integer value) {
            addCriterion("connType <>", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeGreaterThan(Integer value) {
            addCriterion("connType >", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("connType >=", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeLessThan(Integer value) {
            addCriterion("connType <", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeLessThanOrEqualTo(Integer value) {
            addCriterion("connType <=", value, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeIn(List<Integer> values) {
            addCriterion("connType in", values, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeNotIn(List<Integer> values) {
            addCriterion("connType not in", values, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeBetween(Integer value1, Integer value2) {
            addCriterion("connType between", value1, value2, "conntype");
            return (Criteria) this;
        }

        public Criteria andConntypeNotBetween(Integer value1, Integer value2) {
            addCriterion("connType not between", value1, value2, "conntype");
            return (Criteria) this;
        }

        public Criteria andConnidIsNull() {
            addCriterion("connId is null");
            return (Criteria) this;
        }

        public Criteria andConnidIsNotNull() {
            addCriterion("connId is not null");
            return (Criteria) this;
        }

        public Criteria andConnidEqualTo(Long value) {
            addCriterion("connId =", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidNotEqualTo(Long value) {
            addCriterion("connId <>", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidGreaterThan(Long value) {
            addCriterion("connId >", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidGreaterThanOrEqualTo(Long value) {
            addCriterion("connId >=", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidLessThan(Long value) {
            addCriterion("connId <", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidLessThanOrEqualTo(Long value) {
            addCriterion("connId <=", value, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidIn(List<Long> values) {
            addCriterion("connId in", values, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidNotIn(List<Long> values) {
            addCriterion("connId not in", values, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidBetween(Long value1, Long value2) {
            addCriterion("connId between", value1, value2, "connid");
            return (Criteria) this;
        }

        public Criteria andConnidNotBetween(Long value1, Long value2) {
            addCriterion("connId not between", value1, value2, "connid");
            return (Criteria) this;
        }

        public Criteria andConnnameIsNull() {
            addCriterion("connName is null");
            return (Criteria) this;
        }

        public Criteria andConnnameIsNotNull() {
            addCriterion("connName is not null");
            return (Criteria) this;
        }

        public Criteria andConnnameEqualTo(String value) {
            addCriterion("connName =", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameNotEqualTo(String value) {
            addCriterion("connName <>", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameGreaterThan(String value) {
            addCriterion("connName >", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameGreaterThanOrEqualTo(String value) {
            addCriterion("connName >=", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameLessThan(String value) {
            addCriterion("connName <", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameLessThanOrEqualTo(String value) {
            addCriterion("connName <=", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameLike(String value) {
            addCriterion("connName like", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameNotLike(String value) {
            addCriterion("connName not like", value, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameIn(List<String> values) {
            addCriterion("connName in", values, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameNotIn(List<String> values) {
            addCriterion("connName not in", values, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameBetween(String value1, String value2) {
            addCriterion("connName between", value1, value2, "connname");
            return (Criteria) this;
        }

        public Criteria andConnnameNotBetween(String value1, String value2) {
            addCriterion("connName not between", value1, value2, "connname");
            return (Criteria) this;
        }

        public Criteria andOwneridIsNull() {
            addCriterion("ownerId is null");
            return (Criteria) this;
        }

        public Criteria andOwneridIsNotNull() {
            addCriterion("ownerId is not null");
            return (Criteria) this;
        }

        public Criteria andOwneridEqualTo(Long value) {
            addCriterion("ownerId =", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridNotEqualTo(Long value) {
            addCriterion("ownerId <>", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridGreaterThan(Long value) {
            addCriterion("ownerId >", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridGreaterThanOrEqualTo(Long value) {
            addCriterion("ownerId >=", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridLessThan(Long value) {
            addCriterion("ownerId <", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridLessThanOrEqualTo(Long value) {
            addCriterion("ownerId <=", value, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridIn(List<Long> values) {
            addCriterion("ownerId in", values, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridNotIn(List<Long> values) {
            addCriterion("ownerId not in", values, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridBetween(Long value1, Long value2) {
            addCriterion("ownerId between", value1, value2, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwneridNotBetween(Long value1, Long value2) {
            addCriterion("ownerId not between", value1, value2, "ownerid");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNull() {
            addCriterion("owner is null");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNotNull() {
            addCriterion("owner is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerEqualTo(String value) {
            addCriterion("owner =", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotEqualTo(String value) {
            addCriterion("owner <>", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThan(String value) {
            addCriterion("owner >", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThanOrEqualTo(String value) {
            addCriterion("owner >=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThan(String value) {
            addCriterion("owner <", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThanOrEqualTo(String value) {
            addCriterion("owner <=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLike(String value) {
            addCriterion("owner like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotLike(String value) {
            addCriterion("owner not like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerIn(List<String> values) {
            addCriterion("owner in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotIn(List<String> values) {
            addCriterion("owner not in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerBetween(String value1, String value2) {
            addCriterion("owner between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotBetween(String value1, String value2) {
            addCriterion("owner not between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeIsNull() {
            addCriterion("requirementtype is null");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeIsNotNull() {
            addCriterion("requirementtype is not null");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeEqualTo(String value) {
            addCriterion("requirementtype =", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeNotEqualTo(String value) {
            addCriterion("requirementtype <>", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeGreaterThan(String value) {
            addCriterion("requirementtype >", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeGreaterThanOrEqualTo(String value) {
            addCriterion("requirementtype >=", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeLessThan(String value) {
            addCriterion("requirementtype <", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeLessThanOrEqualTo(String value) {
            addCriterion("requirementtype <=", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeLike(String value) {
            addCriterion("requirementtype like", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeNotLike(String value) {
            addCriterion("requirementtype not like", value, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeIn(List<String> values) {
            addCriterion("requirementtype in", values, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeNotIn(List<String> values) {
            addCriterion("requirementtype not in", values, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeBetween(String value1, String value2) {
            addCriterion("requirementtype between", value1, value2, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeNotBetween(String value1, String value2) {
            addCriterion("requirementtype not between", value1, value2, "requirementtype");
            return (Criteria) this;
        }

        public Criteria andCareerIsNull() {
            addCriterion("career is null");
            return (Criteria) this;
        }

        public Criteria andCareerIsNotNull() {
            addCriterion("career is not null");
            return (Criteria) this;
        }

        public Criteria andCareerEqualTo(String value) {
            addCriterion("career =", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerNotEqualTo(String value) {
            addCriterion("career <>", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerGreaterThan(String value) {
            addCriterion("career >", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerGreaterThanOrEqualTo(String value) {
            addCriterion("career >=", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerLessThan(String value) {
            addCriterion("career <", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerLessThanOrEqualTo(String value) {
            addCriterion("career <=", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerLike(String value) {
            addCriterion("career like", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerNotLike(String value) {
            addCriterion("career not like", value, "career");
            return (Criteria) this;
        }

        public Criteria andCareerIn(List<String> values) {
            addCriterion("career in", values, "career");
            return (Criteria) this;
        }

        public Criteria andCareerNotIn(List<String> values) {
            addCriterion("career not in", values, "career");
            return (Criteria) this;
        }

        public Criteria andCareerBetween(String value1, String value2) {
            addCriterion("career between", value1, value2, "career");
            return (Criteria) this;
        }

        public Criteria andCareerNotBetween(String value1, String value2) {
            addCriterion("career not between", value1, value2, "career");
            return (Criteria) this;
        }

        public Criteria andCompanyIsNull() {
            addCriterion("company is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIsNotNull() {
            addCriterion("company is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyEqualTo(String value) {
            addCriterion("company =", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotEqualTo(String value) {
            addCriterion("company <>", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThan(String value) {
            addCriterion("company >", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("company >=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThan(String value) {
            addCriterion("company <", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThanOrEqualTo(String value) {
            addCriterion("company <=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLike(String value) {
            addCriterion("company like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotLike(String value) {
            addCriterion("company not like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyIn(List<String> values) {
            addCriterion("company in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotIn(List<String> values) {
            addCriterion("company not in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyBetween(String value1, String value2) {
            addCriterion("company between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotBetween(String value1, String value2) {
            addCriterion("company not between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andHyIsNull() {
            addCriterion("hy is null");
            return (Criteria) this;
        }

        public Criteria andHyIsNotNull() {
            addCriterion("hy is not null");
            return (Criteria) this;
        }

        public Criteria andHyEqualTo(String value) {
            addCriterion("hy =", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyNotEqualTo(String value) {
            addCriterion("hy <>", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyGreaterThan(String value) {
            addCriterion("hy >", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyGreaterThanOrEqualTo(String value) {
            addCriterion("hy >=", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyLessThan(String value) {
            addCriterion("hy <", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyLessThanOrEqualTo(String value) {
            addCriterion("hy <=", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyLike(String value) {
            addCriterion("hy like", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyNotLike(String value) {
            addCriterion("hy not like", value, "hy");
            return (Criteria) this;
        }

        public Criteria andHyIn(List<String> values) {
            addCriterion("hy in", values, "hy");
            return (Criteria) this;
        }

        public Criteria andHyNotIn(List<String> values) {
            addCriterion("hy not in", values, "hy");
            return (Criteria) this;
        }

        public Criteria andHyBetween(String value1, String value2) {
            addCriterion("hy between", value1, value2, "hy");
            return (Criteria) this;
        }

        public Criteria andHyNotBetween(String value1, String value2) {
            addCriterion("hy not between", value1, value2, "hy");
            return (Criteria) this;
        }

        public Criteria andColumnpathIsNull() {
            addCriterion("columnPath is null");
            return (Criteria) this;
        }

        public Criteria andColumnpathIsNotNull() {
            addCriterion("columnPath is not null");
            return (Criteria) this;
        }

        public Criteria andColumnpathEqualTo(String value) {
            addCriterion("columnPath =", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathNotEqualTo(String value) {
            addCriterion("columnPath <>", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathGreaterThan(String value) {
            addCriterion("columnPath >", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathGreaterThanOrEqualTo(String value) {
            addCriterion("columnPath >=", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathLessThan(String value) {
            addCriterion("columnPath <", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathLessThanOrEqualTo(String value) {
            addCriterion("columnPath <=", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathLike(String value) {
            addCriterion("columnPath like", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathNotLike(String value) {
            addCriterion("columnPath not like", value, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathIn(List<String> values) {
            addCriterion("columnPath in", values, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathNotIn(List<String> values) {
            addCriterion("columnPath not in", values, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathBetween(String value1, String value2) {
            addCriterion("columnPath between", value1, value2, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumnpathNotBetween(String value1, String value2) {
            addCriterion("columnPath not between", value1, value2, "columnpath");
            return (Criteria) this;
        }

        public Criteria andColumntypeIsNull() {
            addCriterion("columnType is null");
            return (Criteria) this;
        }

        public Criteria andColumntypeIsNotNull() {
            addCriterion("columnType is not null");
            return (Criteria) this;
        }

        public Criteria andColumntypeEqualTo(Integer value) {
            addCriterion("columnType =", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeNotEqualTo(Integer value) {
            addCriterion("columnType <>", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeGreaterThan(Integer value) {
            addCriterion("columnType >", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("columnType >=", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeLessThan(Integer value) {
            addCriterion("columnType <", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeLessThanOrEqualTo(Integer value) {
            addCriterion("columnType <=", value, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeIn(List<Integer> values) {
            addCriterion("columnType in", values, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeNotIn(List<Integer> values) {
            addCriterion("columnType not in", values, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeBetween(Integer value1, Integer value2) {
            addCriterion("columnType between", value1, value2, "columntype");
            return (Criteria) this;
        }

        public Criteria andColumntypeNotBetween(Integer value1, Integer value2) {
            addCriterion("columnType not between", value1, value2, "columntype");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andPicpathIsNull() {
            addCriterion("picPath is null");
            return (Criteria) this;
        }

        public Criteria andPicpathIsNotNull() {
            addCriterion("picPath is not null");
            return (Criteria) this;
        }

        public Criteria andPicpathEqualTo(String value) {
            addCriterion("picPath =", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathNotEqualTo(String value) {
            addCriterion("picPath <>", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathGreaterThan(String value) {
            addCriterion("picPath >", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathGreaterThanOrEqualTo(String value) {
            addCriterion("picPath >=", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathLessThan(String value) {
            addCriterion("picPath <", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathLessThanOrEqualTo(String value) {
            addCriterion("picPath <=", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathLike(String value) {
            addCriterion("picPath like", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathNotLike(String value) {
            addCriterion("picPath not like", value, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathIn(List<String> values) {
            addCriterion("picPath in", values, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathNotIn(List<String> values) {
            addCriterion("picPath not in", values, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathBetween(String value1, String value2) {
            addCriterion("picPath between", value1, value2, "picpath");
            return (Criteria) this;
        }

        public Criteria andPicpathNotBetween(String value1, String value2) {
            addCriterion("picPath not between", value1, value2, "picpath");
            return (Criteria) this;
        }

        public Criteria andTagLikeInsensitive(String value) {
            addCriterion("upper(tag) like", value.toUpperCase(), "tag");
            return (Criteria) this;
        }

        public Criteria andConnnameLikeInsensitive(String value) {
            addCriterion("upper(connName) like", value.toUpperCase(), "connname");
            return (Criteria) this;
        }

        public Criteria andOwnerLikeInsensitive(String value) {
            addCriterion("upper(owner) like", value.toUpperCase(), "owner");
            return (Criteria) this;
        }

        public Criteria andRequirementtypeLikeInsensitive(String value) {
            addCriterion("upper(requirementtype) like", value.toUpperCase(), "requirementtype");
            return (Criteria) this;
        }

        public Criteria andCareerLikeInsensitive(String value) {
            addCriterion("upper(career) like", value.toUpperCase(), "career");
            return (Criteria) this;
        }

        public Criteria andCompanyLikeInsensitive(String value) {
            addCriterion("upper(company) like", value.toUpperCase(), "company");
            return (Criteria) this;
        }

        public Criteria andAddressLikeInsensitive(String value) {
            addCriterion("upper(address) like", value.toUpperCase(), "address");
            return (Criteria) this;
        }

        public Criteria andHyLikeInsensitive(String value) {
            addCriterion("upper(hy) like", value.toUpperCase(), "hy");
            return (Criteria) this;
        }

        public Criteria andColumnpathLikeInsensitive(String value) {
            addCriterion("upper(columnPath) like", value.toUpperCase(), "columnpath");
            return (Criteria) this;
        }

        public Criteria andUrlLikeInsensitive(String value) {
            addCriterion("upper(url) like", value.toUpperCase(), "url");
            return (Criteria) this;
        }

        public Criteria andPicpathLikeInsensitive(String value) {
            addCriterion("upper(picPath) like", value.toUpperCase(), "picpath");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        public Criteria() {
            super();
        }
    }

    public static class Criterion {
        public String condition;

        public Object value;

        public Object secondValue;

        public boolean noValue;

        public boolean singleValue;

        public boolean betweenValue;

        public boolean listValue;

        public String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        public Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        public Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        public Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        public Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}