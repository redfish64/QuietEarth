package com.rareventure.quietcraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Taken from Bukkit-WarpPortals/src/main/java/com/mccraftaholics/warpportals/helpers/BlockCrawler.java
 * Given a block type, finds all blocks of same type nearby
 */
public class BlockCrawler {

	public static final int[][] ADJ_LOC = new int[][] { new int[] { -1, 0, 0 }, new int[] { 1, 0, 0 }, new int[] { 0, -1, 0 }, new int[] { 0, 1, 0 },
			new int[] { 0, 0, -1 }, new int[] { 0, 0, 1 } };

	public static final int DEFAULT_MAX_SIZE = 1000;

	int mMaxPortalSize;
	Block mOrigBlock;
	ArrayList<Coords> mProcessedBlocks;

	public BlockCrawler(int maxPortalSize) {
		mMaxPortalSize = maxPortalSize;
	}

	public void start(Block origBlock, ArrayList<Coords> blockCoordsArr) throws MaxRecursionException {
		mOrigBlock = origBlock;
		mProcessedBlocks = blockCoordsArr;
		processAdjacent(mOrigBlock, mOrigBlock.getType());
	}

	private void processAdjacent(Block block, Material type) throws MaxRecursionException {
		if (block != null && block.getType() == type) {
			if (!mProcessedBlocks.contains(new Coords(block))) {
				mProcessedBlocks.add(new Coords(block));
				for (int i = 0; i < ADJ_LOC.length; i++) {
					Location nextLoc = block.getLocation();
					nextLoc.setX(block.getX() + ADJ_LOC[i][0]);
					nextLoc.setY(block.getY() + ADJ_LOC[i][1]);
					nextLoc.setZ(block.getZ() + ADJ_LOC[i][2]);
					if (mProcessedBlocks.size() < mMaxPortalSize)
						processAdjacent(nextLoc.getBlock(), block.getType());
					else
					{
						Bukkit.getLogger().warning("Reached max block count");
					}
						return;
				}
			}
		}
	}

	public static class MaxRecursionException extends Exception {
		public MaxRecursionException(String string) {
			super(string);
		}

		private static final long serialVersionUID = 6836498011849066748L;

	}

}
