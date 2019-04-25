package org.austin.mergeInstruments;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.austin.mergeInstruments.instrument.Instrument;
import org.austin.mergeInstruments.instrument.InstrumentContent;
import org.austin.mergeInstruments.instrument.LMEInstrument;
import org.austin.mergeInstruments.instrument.PrimeInstrument;
import org.austin.mergeInstruments.rule.MergedRule;
import org.austin.mergeInstruments.rule.Rule;

public class InstrumentMergeSimulate {
	public static void main(String[] args) {
		BlockingDeque<InstrumentContent> queue = new LinkedBlockingDeque<InstrumentContent>();
		Instrument lme = new LMEInstrument();
		Instrument prime = new PrimeInstrument();
		try {
			InstrumentContent insContent = lme.publish();
			System.out.println("LME:");
			System.out.println(insContent.getContent());
			queue.put(insContent);
			
			StringBuilder content = new StringBuilder();
			content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |"+"\n");
			content.append("|  15-07-2018        | 17-07-2018    |  LME_PB   | Lead 13 March 2018 |");
			insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);
			System.out.println(insContent.getContent());
			queue.put(insContent);
			
			insContent = prime.publish();
			System.out.println("PRIME:");
			System.out.println(insContent.getContent());
			queue.put(insContent);
			
			InstrumentMeger  merger = new InstrumentMeger();
			Rule.mapIns.put(Instrument.PRIME, Instrument.LME);
			merger.setRule(new MergedRule());
			while(!queue.isEmpty()) {
				System.out.println("Result:");
				System.out.println(merger.publish(queue.poll()));
			}
			System.out.println("end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
