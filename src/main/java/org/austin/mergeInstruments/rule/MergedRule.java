package org.austin.mergeInstruments.rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.austin.mergeInstruments.instrument.InstrumentContent;

public class MergedRule implements Rule {
	private final Map<String, Map<String, String>> mapLastMergedData = new ConcurrentHashMap<>();

	@Override
	public String parse(InstrumentContent trade) throws IOException {
		Map<String, String> lastInsTrade = null;
		// If the instrument is to merged to other instrument
		if (mapIns.containsValue(trade.getInsCode())) {
			lastInsTrade = mapLastMergedData.get(trade.getInsCode());
			if (lastInsTrade != null) {
				lastInsTrade.put(trade.getName(), trade.getContent());
			} else {
				lastInsTrade = new ConcurrentHashMap<>();
				lastInsTrade.put(trade.getName(), trade.getContent());
				mapLastMergedData.put(trade.getInsCode(), lastInsTrade);
			}
			return format(trade.getContent(), trade.isTradable());
		}
		lastInsTrade = mapLastMergedData.get(mapIns.get(trade.getInsCode()));
		
		String lastTradeContent = null;
		if(lastInsTrade != null) {
			lastTradeContent = lastInsTrade.get(getExchangeCode(trade));
		}

		return merge(trade.getContent(), lastTradeContent, trade.isTradable());
	}

	private String merge(String src, String target, boolean tradable) throws IOException {
		BufferedReader s = new BufferedReader(new StringReader(src));
		BufferedReader t = null;
		try {
			String lastTradeDate = null;
			String deliverDate = null;
			String tline = null;
			String lastLine = null;
			if (target != null && !"".equals(target)) {
				t = new BufferedReader(new StringReader(target));
				while ((tline = t.readLine()) != null) {
					lastLine = tline;
				}
				String[] tElements = lastLine.split("\\|");

				if (tElements.length == 5) {
					lastTradeDate = tElements[1];
					deliverDate = tElements[2];
				}
			}
			String[] sContent = new String[2];
			int i = 0;
			while (i < 2) {
				sContent[i++] = s.readLine();
			}
			String[] sElements = sContent[0].split("\\|");
			sContent[0] = "";
			for(int j = 1; j < sElements.length; j++) {
				if(j == 5) {continue;}
				sContent[0] += ("|"+sElements[j]);
			}
			sContent[0] += "|";
			sElements = sContent[1].split("\\|");
			if (sElements.length == 7) {
				sElements[1] = lastTradeDate != null ? lastTradeDate : sElements[1];
				sElements[2] = deliverDate != null ? deliverDate : sElements[2];
				String blank = "";
				int indexUnderLine = sElements[3].indexOf("_");
				sElements[3] = sElements[3].substring(indexUnderLine + 1, sElements[3].length());
				while (indexUnderLine >= 0) {
					blank += " ";
					indexUnderLine--;
				}
				sElements[3] = blank+sElements[3];
			}
			sContent[1] = "";
			for (int j = 1; j < sElements.length; j++) {
				if (j == 5)
					continue;
				sContent[1] += ("|" + sElements[j]);
			}
			sContent[1] += "|";
			return sContent[0] + "\n" + sContent[1];
		} finally {
			if(s != null) s.close();
			if(t != null) t.close();
		}

	}

	private String format(String src, boolean tradable) throws IOException {
		BufferedReader s = new BufferedReader(new StringReader(src));
		String[] sContent = new String[2];
		int i = 0;
		while (i < 2) {
			sContent[i++] = s.readLine();
		}
		sContent[0] += " TRADABLE |";
		String[] sElements = sContent[1].split("\\|");
		if (sElements.length == 5) {
			String blank = "";
			int indexUnderLine = sElements[3].indexOf("_");
			sElements[3] = sElements[3].substring(indexUnderLine + 1, sElements[3].length());
			while (indexUnderLine >= 0) {
				blank += " ";
				indexUnderLine--;
			}
			sElements[3] = blank+sElements[3];
		}
		sContent[1] = "";
		for (int j = 1; j < sElements.length; j++) {
			if (j == 5)
				continue;
			sContent[1] += ("|" + sElements[j]);
		}
		sContent[1] += tradable ? "| TRUE     |" : "|  FALSE    |";
		s.close();
		return sContent[0] + "\n" + sContent[1];
	}

	private String getExchangeCode(InstrumentContent trade) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(trade.getContent()));
		String tline = null;
		int i = 0;
		while (i < 2) {
			tline = br.readLine();
			i++;
		}
		String[] elements = tline.split("\\|");
		if (elements.length >= 6) {
			return elements[5].trim();
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		// | LAST_TRADING_DATE
		String str = "| Last_Trade | deliver |" + "\n" + "|1|2|";
		StringReader sr = new StringReader(str);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println(br.lines().count());
//		String[] e = br.readLine().split("\\|");
//		System.out.println(e.length + e[0] + " " + e[1]);
	}
}
