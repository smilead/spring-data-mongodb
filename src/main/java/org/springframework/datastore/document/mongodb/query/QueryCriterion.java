package org.springframework.datastore.document.mongodb.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.datastore.document.InvalidDocumentStoreApiUageException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class QueryCriterion {
	
	private QueryBuilder qb = null;
	
	private LinkedHashMap<String, Object> criteria = new LinkedHashMap<String, Object>();

	private Object isValue = null;
	
	
	public QueryCriterion(QueryBuilder qb) {
		super();
		this.qb = qb;
	}


	public QueryCriterion is(Object o) {
		if (isValue != null) {
			throw new InvalidDocumentStoreApiUageException("Multiple 'is' values declared.");
		}
		this.isValue = o;
		return this;
	}

	public QueryCriterion lt(Object o) {
		criteria.put("$lt", o);
		return this;
	}
	
	public QueryCriterion lte(Object o) {
		criteria.put("$lte", o);
		return this;
	}
	
	public QueryCriterion gt(Object o) {
		criteria.put("$gt", o);
		return this;
	}
	
	public QueryCriterion gte(Object o) {
		criteria.put("$gte", o);
		return this;
	}
	
	public QueryCriterion in(Object... o) {
		criteria.put("$in", o);
		return this;
	}

	public QueryCriterion nin(Object... o) {
		criteria.put("$min", o);
		return this;
	}

	public QueryCriterion mod(Number value, Number remainder) {
		List<Object> l = new ArrayList<Object>();
		l.add(value);
		l.add(remainder);
		criteria.put("$mod", l);
		return this;
	}

	public QueryCriterion all(Object o) {
		criteria.put("$is", o);
		return this;
	}

	public QueryCriterion size(Object o) {
		criteria.put("$is", o);
		return this;
	}

	public QueryCriterion exists(boolean b) {
		return this;
	}

	public QueryCriterion type(int t) {
		return this;
	}

	public QueryCriterion not() {
		criteria.put("$not", null);
		return this;
	}
	
	public QueryCriterion regExp(String re) {
		return this;
	}

	public void or(List<Query> queries) {
		criteria.put("$or", queries);		
	}
	
	public Query build() {
		return qb.build(); 
	}

	public DBObject getCriteriaObject(String key) {
		DBObject dbo = new BasicDBObject();
		boolean not = false;
		for (String k : criteria.keySet()) {
			if (not) {
				DBObject notDbo = new BasicDBObject();
				notDbo.put(k, criteria.get(k));
				dbo.put("$not", notDbo);
				not = false;
			}
			else {
				if ("$not".equals(k)) {
					not = true;
				}
				else {
					dbo.put(k, criteria.get(k));
				}
			}
		}
		DBObject queryCriteria = new BasicDBObject();
		if (isValue != null) {
			queryCriteria.put(key, isValue);
			queryCriteria.putAll(dbo);
		}
		else {
			queryCriteria.put(key, dbo);
		}
		return queryCriteria;
	}

}
