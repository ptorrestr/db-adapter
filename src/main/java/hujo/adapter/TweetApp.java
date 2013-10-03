package hujo.adapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.s4.base.KeyFinder;
import org.apache.s4.core.App;
import org.apache.s4.core.Stream;
import org.apache.s4.core.ft.CheckpointingConfig;
import org.apache.s4.core.ft.CheckpointingConfig.CheckpointingMode;

import com.google.common.collect.ImmutableList;

public class TweetApp extends App{

	@Override
	protected void onClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onInit() {
		try {
			ReporterTweetSlidePE reporterTweetSlide = createPE(ReporterTweetSlidePE.class);
			reporterTweetSlide.setTimerInterval(10, TimeUnit.SECONDS);
			reporterTweetSlide.setCheckpointingConfig(
					new CheckpointingConfig.Builder(CheckpointingMode.TIME)
						.frequency(20).timeUnit(TimeUnit.SECONDS).build());
			Stream<TweetSlideEvent> aggregatedTweetSlide = 
				createStream("AggregatedTweetSlide", new KeyFinder<TweetSlideEvent>() {
					@Override
					public List<String> get(final TweetSlideEvent arg0) {
						return ImmutableList.of(arg0.getSlide());
					}
				}, reporterTweetSlide);
			
			TweetExtractorPE tweetExtractorPE = createPE(TweetExtractorPE.class);
			tweetExtractorPE.setDownStream(aggregatedTweetSlide);
			tweetExtractorPE.setSingleton(true);
			createInputStream("RawStatus", tweetExtractorPE);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}
	
}
