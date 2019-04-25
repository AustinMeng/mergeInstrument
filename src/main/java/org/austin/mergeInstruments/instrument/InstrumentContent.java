package org.austin.mergeInstruments.instrument;

public class InstrumentContent {
	private String insCode;
	private String name;
	private String content;
	private boolean tradable;
	public InstrumentContent(String insCode, String name, String content, boolean tradable) {
		this.insCode = insCode;
		this.name = name;
		this.content = content;
		this.setTradable(tradable);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInsCode() {
		return insCode;
	}
	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}
	public boolean isTradable() {
		return tradable;
	}
	public void setTradable(boolean tradable) {
		this.tradable = tradable;
	}
}
