/*
 * Copyright (C) 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.note.rteditor.media.choose.processor;

import java.io.IOException;
import java.io.InputStream;

import com.utils.note.rteditor.api.RTMediaFactory;
import com.utils.note.rteditor.api.media.RTAudio;
import com.utils.note.rteditor.api.media.RTImage;
import com.utils.note.rteditor.api.media.RTMediaSource;
import com.utils.note.rteditor.api.media.RTMediaType;
import com.utils.note.rteditor.api.media.RTVideo;

public class ImageProcessor extends MediaProcessor {

    public interface ImageProcessorListener extends MediaProcessorListener {
        public void onImageProcessed(RTImage image);
    }

    private ImageProcessorListener mListener;

    public ImageProcessor(String originalFile, RTMediaFactory<RTImage, RTAudio, RTVideo> mediaFactory, ImageProcessorListener listener) {
        super(originalFile, mediaFactory, listener);
        mListener = listener;
    }

    @Override
    protected void processMedia() throws IOException, Exception {
        InputStream in = super.getInputStream();
        if (in == null) {
            if (mListener != null) {
                mListener.onError("No file found to process");
            }
        } else {
            RTMediaSource source = new RTMediaSource(RTMediaType.IMAGE, in, getOriginalFile(), getMimeType());
            RTImage image = mMediaFactory.createImage(source);
            if (image != null && mListener != null) {
                mListener.onImageProcessed(image);
            }
        }

    }

}