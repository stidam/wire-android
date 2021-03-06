/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.core.controllers.tracking.events.media;


import android.support.annotation.NonNull;
import com.waz.api.IConversation;
import com.waz.zclient.core.controllers.tracking.attributes.Attribute;
import com.waz.zclient.core.controllers.tracking.attributes.RangedAttribute;
import com.waz.zclient.core.controllers.tracking.events.Event;

public class SentAudioMessageEvent extends Event {

    public enum AudioEffectType {
        NONE("none"),
        HELIUM("helium"),
        JELLY_FISH("jellyfish"),
        HARE("hare"),
        CATHEDRAL("cathedral"),
        ALIEN("alien"),
        ROBOT("robot"),
        UPSIDE_DOWN("upsidedown")
        ;

        public final String nameString;
        AudioEffectType(String nameString) {
            this.nameString = nameString;
        }
    }

    public SentAudioMessageEvent(int durationSec, AudioEffectType audioEffectType, boolean fromSlideUp, boolean fromMinimised, IConversation conversation) {
        String context = fromSlideUp ? "slide_up" : "after_preview";
        attributes.put(Attribute.CONTEXT, context);
        attributes.put(Attribute.STATE, fromMinimised ? "minimised" : "keyboard");
        attributes.put(Attribute.EFFECT, audioEffectType.nameString);
        rangedAttributes.put(RangedAttribute.VIDEO_AND_AUDIO_MESSAGE_DURATION, durationSec);
        if (conversation != null) {
            attributes.put(Attribute.WITH_BOT, String.valueOf(conversation.isOtto()));
            attributes.put(Attribute.CONVERSATION_TYPE, conversation.getName());
            attributes.put(Attribute.IS_EPHEMERAL, String.valueOf(conversation.isEphemeral()));
            attributes.put(Attribute.EPHEMERAL_EXPIRATION, String.valueOf(conversation.getEphemeralExpiration().duration().toSeconds()));
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "media.sent_audio_message";
    }
}
