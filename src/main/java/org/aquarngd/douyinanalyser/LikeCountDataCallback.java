/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser;

@FunctionalInterface
public interface LikeCountDataCallback {
    void onLikeCountRetrieved(int userid, int likecount);
}
