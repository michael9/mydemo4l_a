package com.cqvip.dao;

/**
 * 主键
 * @author luojiang
 *
 */
public class PrimaryKey {
	/**
	 * 主键数组
	 */
	private String[] keyNames = null;
	/**
	 * 主键是否自动增长
	 */
	private boolean  autoInc  = false;

	public PrimaryKey(String keyName) {
		keyNames = new String[]{keyName};
	}
	
	public PrimaryKey(String keyName, boolean autoInc) {
		keyNames = new String[]{keyName};
		this.autoInc = autoInc;
	}
	
	public PrimaryKey(String[] keyNames) {
		this.keyNames = keyNames;
	}
	
	public String[] getKeyNames() {
		return this.keyNames;
	}
	
	public boolean isAutoInc() {
		return this.autoInc;
	}
}