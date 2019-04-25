package org.austin.mergeInstruments;

import java.io.IOException;

import org.austin.mergeInstruments.instrument.InstrumentContent;
import org.austin.mergeInstruments.rule.Rule;

public class InstrumentMeger {
	private Rule rule;

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public String publish(InstrumentContent insContent) throws IOException {
		return rule.parse(insContent);
	}

}
