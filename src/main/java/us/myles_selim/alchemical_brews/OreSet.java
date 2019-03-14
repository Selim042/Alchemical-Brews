package us.myles_selim.alchemical_brews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.oredict.OreDictionary;

public class OreSet {

	public final String ore;
	public final String ingot;
	public final String resource;
	public final String name;

	public OreSet(String ore, String ingot, String resource, String name) {
		this.ore = ore;
		this.ingot = ingot;
		this.resource = resource;
		this.name = name;
	}

	private static final String[] PREFIXES = new String[] { "ingot", "gem", "coal", "dust" };
	private static List<OreSet> SETS;

	public static List<OreSet> getOreSets() {
		if (SETS != null)
			return SETS;
		List<OreSet> sets = new ArrayList<>();
		sets.add(new OreSet("oreCoal", "coal", "blockCoal", "coal"));
		for (String n : OreDictionary.getOreNames()) {
			String prefix = null;
			for (String oN : PREFIXES) {
				if (n.startsWith(oN)) {
					prefix = oN;
					break;
				}
			}
			if (prefix == null)
				continue;
			String name = n.substring(prefix.length());
			String blockName = "block" + name;
			String oreName = "ore" + name;
			if (!OreDictionary.getOres(blockName).isEmpty() && !OreDictionary.getOres(oreName).isEmpty())
				sets.add(new OreSet(oreName, n, blockName, name.toLowerCase()));
		}
		SETS = Collections.unmodifiableList(sets);
		return SETS;
	}

}
