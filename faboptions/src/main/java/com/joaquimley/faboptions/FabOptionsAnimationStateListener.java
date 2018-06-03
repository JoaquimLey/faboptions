package com.joaquimley.faboptions;

/**
 * FabOptions exposed listener for the expand and collapse animations
 */

public interface FabOptionsAnimationStateListener {
	void onOpenAnimationEnd();

	void onCloseAnimationEnd();
}