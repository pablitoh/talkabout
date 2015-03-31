package com.concon.talkabout.talkabout.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by gconte on 3/19/15.
 */
public class MarryKillParserService extends ParserService {

    @Override
    public List<String> parseXml(int talkLevel, InputStream xml, String tag) throws XmlPullParserException, IOException {
        List<String> data = super.parseXml(talkLevel, xml, tag);
        Random rand = new Random();

        HashSet<String> set = new HashSet<>();
        while (set.size() < 3) {
            set.add(data.get(rand.nextInt(data.size())));
        }

        return new ArrayList<String>(set);
    }

    @Override
    public void parseInnerCustom(XmlPullParser parser, int talkLevel, String tag, List<String> data) throws XmlPullParserException, IOException {

        if (talkLevel == 1) {
            lookingForGirls(parser, data);
        } else if (talkLevel == 2) {
            skip(parser);
            lookingForBoys(parser, data);
        } else {
            lookingForGirlsWhoAreBoysWhoLikeBoysToBeGirlsWhoDoBoysLikeTheyreGirlsWhoDoGirlsLikeTheyreBoysAlwaysShouldBeSomeoneYouReallyLove(parser, data);
        }

    }

    private void lookingForGirls(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        parsePerson(parser, data, "girls");
    }

    private void lookingForBoys(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        parser.nextTag();
        String nextTag = parser.getName();
        parsePerson(parser, data, nextTag);
    }

    private void lookingForGirlsWhoAreBoysWhoLikeBoysToBeGirlsWhoDoBoysLikeTheyreGirlsWhoDoGirlsLikeTheyreBoysAlwaysShouldBeSomeoneYouReallyLove(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        lookingForGirls(parser, data);
        lookingForBoys(parser, data);
    }

    private void parsePerson(XmlPullParser parser, List<String> data, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            data.add(read(parser, "entry"));
        }
    }

}
