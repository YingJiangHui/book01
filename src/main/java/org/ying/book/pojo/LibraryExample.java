package org.ying.book.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LibraryExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
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

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
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

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(BigDecimal value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(BigDecimal value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(BigDecimal value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(BigDecimal value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<BigDecimal> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<BigDecimal> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(BigDecimal value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(BigDecimal value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(BigDecimal value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(BigDecimal value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<BigDecimal> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<BigDecimal> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andCircumferenceIsNull() {
            addCriterion("circumference is null");
            return (Criteria) this;
        }

        public Criteria andCircumferenceIsNotNull() {
            addCriterion("circumference is not null");
            return (Criteria) this;
        }

        public Criteria andCircumferenceEqualTo(Integer value) {
            addCriterion("circumference =", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceNotEqualTo(Integer value) {
            addCriterion("circumference <>", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceGreaterThan(Integer value) {
            addCriterion("circumference >", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceGreaterThanOrEqualTo(Integer value) {
            addCriterion("circumference >=", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceLessThan(Integer value) {
            addCriterion("circumference <", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceLessThanOrEqualTo(Integer value) {
            addCriterion("circumference <=", value, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceIn(List<Integer> values) {
            addCriterion("circumference in", values, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceNotIn(List<Integer> values) {
            addCriterion("circumference not in", values, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceBetween(Integer value1, Integer value2) {
            addCriterion("circumference between", value1, value2, "circumference");
            return (Criteria) this;
        }

        public Criteria andCircumferenceNotBetween(Integer value1, Integer value2) {
            addCriterion("circumference not between", value1, value2, "circumference");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "deleted");
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

        public Criteria andMaxBorrowDaysIsNull() {
            addCriterion("max_borrow_days is null");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysIsNotNull() {
            addCriterion("max_borrow_days is not null");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysEqualTo(Integer value) {
            addCriterion("max_borrow_days =", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysNotEqualTo(Integer value) {
            addCriterion("max_borrow_days <>", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysGreaterThan(Integer value) {
            addCriterion("max_borrow_days >", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_borrow_days >=", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysLessThan(Integer value) {
            addCriterion("max_borrow_days <", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysLessThanOrEqualTo(Integer value) {
            addCriterion("max_borrow_days <=", value, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysIn(List<Integer> values) {
            addCriterion("max_borrow_days in", values, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysNotIn(List<Integer> values) {
            addCriterion("max_borrow_days not in", values, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysBetween(Integer value1, Integer value2) {
            addCriterion("max_borrow_days between", value1, value2, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andMaxBorrowDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("max_borrow_days not between", value1, value2, "maxBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysIsNull() {
            addCriterion("default_borrow_days is null");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysIsNotNull() {
            addCriterion("default_borrow_days is not null");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysEqualTo(Integer value) {
            addCriterion("default_borrow_days =", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysNotEqualTo(Integer value) {
            addCriterion("default_borrow_days <>", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysGreaterThan(Integer value) {
            addCriterion("default_borrow_days >", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("default_borrow_days >=", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysLessThan(Integer value) {
            addCriterion("default_borrow_days <", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysLessThanOrEqualTo(Integer value) {
            addCriterion("default_borrow_days <=", value, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysIn(List<Integer> values) {
            addCriterion("default_borrow_days in", values, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysNotIn(List<Integer> values) {
            addCriterion("default_borrow_days not in", values, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysBetween(Integer value1, Integer value2) {
            addCriterion("default_borrow_days between", value1, value2, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andDefaultBorrowDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("default_borrow_days not between", value1, value2, "defaultBorrowDays");
            return (Criteria) this;
        }

        public Criteria andClosedIsNull() {
            addCriterion("closed is null");
            return (Criteria) this;
        }

        public Criteria andClosedIsNotNull() {
            addCriterion("closed is not null");
            return (Criteria) this;
        }

        public Criteria andClosedEqualTo(Boolean value) {
            addCriterion("closed =", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedNotEqualTo(Boolean value) {
            addCriterion("closed <>", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedGreaterThan(Boolean value) {
            addCriterion("closed >", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("closed >=", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedLessThan(Boolean value) {
            addCriterion("closed <", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedLessThanOrEqualTo(Boolean value) {
            addCriterion("closed <=", value, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedIn(List<Boolean> values) {
            addCriterion("closed in", values, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedNotIn(List<Boolean> values) {
            addCriterion("closed not in", values, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedBetween(Boolean value1, Boolean value2) {
            addCriterion("closed between", value1, value2, "closed");
            return (Criteria) this;
        }

        public Criteria andClosedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("closed not between", value1, value2, "closed");
            return (Criteria) this;
        }

        public Criteria andDisableReserveIsNull() {
            addCriterion("disable_reserve is null");
            return (Criteria) this;
        }

        public Criteria andDisableReserveIsNotNull() {
            addCriterion("disable_reserve is not null");
            return (Criteria) this;
        }

        public Criteria andDisableReserveEqualTo(Boolean value) {
            addCriterion("disable_reserve =", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveNotEqualTo(Boolean value) {
            addCriterion("disable_reserve <>", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveGreaterThan(Boolean value) {
            addCriterion("disable_reserve >", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveGreaterThanOrEqualTo(Boolean value) {
            addCriterion("disable_reserve >=", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveLessThan(Boolean value) {
            addCriterion("disable_reserve <", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveLessThanOrEqualTo(Boolean value) {
            addCriterion("disable_reserve <=", value, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveIn(List<Boolean> values) {
            addCriterion("disable_reserve in", values, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveNotIn(List<Boolean> values) {
            addCriterion("disable_reserve not in", values, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_reserve between", value1, value2, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableReserveNotBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_reserve not between", value1, value2, "disableReserve");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowIsNull() {
            addCriterion("disable_borrow is null");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowIsNotNull() {
            addCriterion("disable_borrow is not null");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowEqualTo(Boolean value) {
            addCriterion("disable_borrow =", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowNotEqualTo(Boolean value) {
            addCriterion("disable_borrow <>", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowGreaterThan(Boolean value) {
            addCriterion("disable_borrow >", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowGreaterThanOrEqualTo(Boolean value) {
            addCriterion("disable_borrow >=", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowLessThan(Boolean value) {
            addCriterion("disable_borrow <", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowLessThanOrEqualTo(Boolean value) {
            addCriterion("disable_borrow <=", value, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowIn(List<Boolean> values) {
            addCriterion("disable_borrow in", values, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowNotIn(List<Boolean> values) {
            addCriterion("disable_borrow not in", values, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_borrow between", value1, value2, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableBorrowNotBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_borrow not between", value1, value2, "disableBorrow");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationIsNull() {
            addCriterion("disable_reserve_application is null");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationIsNotNull() {
            addCriterion("disable_reserve_application is not null");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationEqualTo(Boolean value) {
            addCriterion("disable_reserve_application =", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationNotEqualTo(Boolean value) {
            addCriterion("disable_reserve_application <>", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationGreaterThan(Boolean value) {
            addCriterion("disable_reserve_application >", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationGreaterThanOrEqualTo(Boolean value) {
            addCriterion("disable_reserve_application >=", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationLessThan(Boolean value) {
            addCriterion("disable_reserve_application <", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationLessThanOrEqualTo(Boolean value) {
            addCriterion("disable_reserve_application <=", value, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationIn(List<Boolean> values) {
            addCriterion("disable_reserve_application in", values, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationNotIn(List<Boolean> values) {
            addCriterion("disable_reserve_application not in", values, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_reserve_application between", value1, value2, "disableReserveApplication");
            return (Criteria) this;
        }

        public Criteria andDisableReserveApplicationNotBetween(Boolean value1, Boolean value2) {
            addCriterion("disable_reserve_application not between", value1, value2, "disableReserveApplication");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

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

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
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

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}