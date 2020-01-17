package bane.innovation.a19kworddictionary.util;

public class Word {

	private String key,value;
	private int id;
	
	public Word( int id, String key, String value) {
		super();
		this.key = key;
		this.value = value;
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
