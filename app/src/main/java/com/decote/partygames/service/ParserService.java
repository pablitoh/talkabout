package com.decote.partygames.service;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gconte on 3/16/15.
 */
public abstract class ParserService {

    public static final String ns = null;

    public XmlPullParser xmlPullParser;

    public List<String> parseXml(int talkLevel, InputStream xml, String tag) throws XmlPullParserException, IOException {
        xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(xml, null);
        xmlPullParser.nextTag();
        return readFeed(xmlPullParser, talkLevel, tag);
    }

    public List readFeed(XmlPullParser parser, int talkLevel, String tag) throws XmlPullParserException, IOException {
        List<String> data = new ArrayList();
        boolean processed = false;
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if(processed) {
                break;
            }

            parseInnerCustom(parser, talkLevel, tag, data);
            processed = true;
        }
        return data;
    }

    public abstract void parseInnerCustom(XmlPullParser parser, int talkLevel, String tag, List<String> data) throws XmlPullParserException, IOException;

    public String read(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return text;
    }

    public String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}