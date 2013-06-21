package com.cqvip.dao;

/**
 * ����
 * @author luojiang
 *
 */
public class PrimaryKey {
	/**
	 * ��������
	 */
	private String[] keyNames = null;
	/**
	 * �����Ƿ��Զ�����
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