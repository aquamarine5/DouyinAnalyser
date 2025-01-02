package org.aquarngd.douyinanalyser;

@FunctionalInterface
public interface LikeCountDataCallback {
    void onLikeCountRetrieved(int userid, int likecount);
}
