package com.concon.talkabout.talkabout.dataType;

import android.content.Context;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.service.INeverParserService;
import com.concon.talkabout.talkabout.service.SingleFeedParserService;
import com.concon.talkabout.talkabout.utils.DbManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by OE on 14/05/2015.
 */
public class OptionsMap {



    public interface RewardRunner
    {
        public RewardCard getReward();
    }
    private INeverParserService iNeverParserService;
    private SingleFeedParserService singleFeedParserService;
    Random random = new Random();
    HashMap<Integer, RewardRunner> OPTIONS_MAP;
    private Context mContext;
    private List<String> chaosRules, iNever, randomFacts, superRules;


    public OptionsMap(Context mContext)
    {
     this.mContext = mContext;
        iNeverParserService = new INeverParserService();
        try {
            iNever = iNeverParserService.parseXml(2, mContext.getResources().openRawResource(R.raw.inever), "inever");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        singleFeedParserService = new SingleFeedParserService();

        try {
            chaosRules = singleFeedParserService.parseXml(2, mContext.getResources().openRawResource(R.raw.chaosrule), "chaos");
            DbManager db = new DbManager(mContext);
            chaosRules.addAll(db.getAllPhrasesAsArray());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            randomFacts = singleFeedParserService.parseXml(2, mContext.getResources().openRawResource(R.raw.randomfacts), "randomfacts");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            superRules = singleFeedParserService.parseXml(2, mContext.getResources().openRawResource(R.raw.super_rules), "superrules");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer,RewardRunner> getMap()
    {
        if(OPTIONS_MAP==null)
        {
            OPTIONS_MAP = new HashMap<Integer, RewardRunner>();
            OPTIONS_MAP.put(0, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.chaosTitle), chaosRules.get(random.nextInt(chaosRules.size())), R.drawable.icon_skull);
                }
            });
            OPTIONS_MAP.put(1,new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.sacrificeTitle), mContext.getString(R.string.sacrifice), R.drawable.icon_blood);
                }
            });
            OPTIONS_MAP.put(2, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.randomTitle), randomFacts.get(random.nextInt(randomFacts.size())), R.drawable.icon_question);
                }
            });
            OPTIONS_MAP.put(3, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.chaosTitle), chaosRules.get(random.nextInt(chaosRules.size())), R.drawable.icon_skull);
                }
            });
            OPTIONS_MAP.put(4, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.vendettaTitle), mContext.getString(R.string.vendetta), R.drawable.icon_vendetta);
                }
            });
            OPTIONS_MAP.put(5, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.cleanseTitle), mContext.getString(R.string.cleanse), R.drawable.icon_broom);
                }
            });
            OPTIONS_MAP.put(6, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.chaosTitle), chaosRules.get(random.nextInt(chaosRules.size())), R.drawable.icon_skull);
                }
            });
            OPTIONS_MAP.put(7, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.drinkTitle), mContext.getString(R.string.oneShot), R.drawable.icon_drink);
                }
            });
            OPTIONS_MAP.put(8, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.randomTitle), randomFacts.get(random.nextInt(randomFacts.size())), R.drawable.icon_question);
                }
            });
            OPTIONS_MAP.put(9, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return  new RewardCard(mContext.getString(R.string.iNeverPostTitle), iNever.get(random.nextInt(iNever.size())), R.drawable.icon_never);
                }
            });
            OPTIONS_MAP.put(10, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.globalTitle), mContext.getString(R.string.global), R.drawable.icon_world);
                }
            });
            OPTIONS_MAP.put(11, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.targetTitle), mContext.getString(R.string.target), R.drawable.icon_target);
                }
            });
            OPTIONS_MAP.put(12, new RewardRunner() {
                @Override
                public RewardCard getReward() {
                    return new RewardCard(mContext.getString(R.string.devilTitle),  superRules.get(random.nextInt(superRules.size())), R.drawable.devil);
                }
            });
        }
        return OPTIONS_MAP;
    }



}
