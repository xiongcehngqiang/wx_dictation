package indi.cyken.dao;

import java.util.List;

import indi.cyken.domain.Unit;

public interface UnitDao {

	List<Unit> getByBid(String bookid) throws Exception;

}
