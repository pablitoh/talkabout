package com.decote.partygames.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by gconte on 3/19/15.
 */
public class SingleFeedParserService extends ParserService{

    @Override
    public void parseInnerCustom(XmlPullParser parser, int talkLevel, String tag, List<String> data) throws XmlPullParserException, IOException {
        String name = parser.getName();
        parser.require(XmlPullParser.START_TAG, ns, name);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            data.add(read(parser, "entry"));
        }
    }
}
