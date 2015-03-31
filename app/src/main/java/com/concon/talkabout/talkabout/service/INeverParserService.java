package com.concon.talkabout.talkabout.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by gconte on 3/19/15.
 */
public class INeverParserService extends ParserService {

    @Override
    public void parseInnerCustom(XmlPullParser parser, int talkLevel, String tag, List<String> data) throws XmlPullParserException, IOException {

        if (talkLevel == 1) {
            getSoft(parser, data);
        } else if (talkLevel == 2) {
            skip(parser);
            getHot(parser, data);
        } else {
            getBoth(parser, data);
        }

    }

    private void getSoft(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        parseEntry(parser, data, "soft");
    }

    private void getHot(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        parser.nextTag();
        String nextTag = parser.getName();
        parseEntry(parser, data, nextTag);
    }

    private void getBoth(XmlPullParser parser, List<String> data) throws IOException, XmlPullParserException {
        getSoft(parser, data);
        getHot(parser, data);
    }

    private void parseEntry(XmlPullParser parser, List<String> data, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            data.add(read(parser, "entry"));
        }
    }
}
