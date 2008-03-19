/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.clientapp;

/**
 * Implementation of DeckFeed cointaining first chapter of 
 * "Dorothy and the Wizard in Oz" by L. Frank Baum.
 */
public class DeckWizardInOzFeed implements DeckFeed {
    /**
     * The static content of all pages.
     */
    private final static String[] pages = {
        "<h1 style=\"text-align:center\">Dorothy and the Wizard in Oz</h1>"+
        "<h2 style=\"text-align:center\">by L. Frank Baum</h2>",
        
        "<h4 style=\"text-align:center\">Chapter 1: The Earthquake</h4>"+
        "<p>The train from &apos;Frisco was very late.  It should have arrived at "+
        "Hugson&apos;s Siding at midnight, but it was already five o&apos;clock and the "+
        "gray dawn was breaking in the east when the little train slowly "+
        "rumbled up to the open shed that served for the station-house.  As it "+
        "came to a stop the conductor called out in a loud voice:</p> "+
        "<p>&quot;Hugson&apos;s Siding!&quot;</p>",

        "<p>At once a little girl rose from her seat and walked to the door of the "+
        "car, carrying a wicker suit-case in one hand and a round bird-cage "+
        "covered up with newspapers in the other, while a parasol was tucked "+
        "under her arm.  The conductor helped her off the car and then the "+
        "engineer started his train again, so that it puffed and groaned and "+
        "moved slowly away up the track.  The reason he was so late was because "+
        "all through the night there were times when the solid earth shook and "+
        "trembled under him, and the engineer was afraid that at any moment the "+
        "rails might spread apart and an accident happen to his passengers.  So "+
        "he moved the cars slowly and with caution.</p>",

        "<p>The little girl stood still to watch until the train had disappeared "+
        "around a curve; then she turned to see where she was.</p> "+
        "<p>The shed at Hugson&apos;s Siding was bare save for an old wooden bench, and "+
        "did not look very inviting.  As she peered through the soft gray light "+
        "not a house of any sort was visible near the station, nor was any "+
        "person in sight; but after a while the child discovered a horse and "+
        "buggy standing near a group of trees a short distance away.  She "+
        "walked toward it and found the horse tied to a tree and standing "+
        "motionless, with its head hanging down almost to the ground.  It was a "+
        "big horse, tall and bony, with long legs and large knees and feet. "+
        "She could count his ribs easily where they showed through the skin of "+
        "his body, and his head was long and seemed altogether too big for him, "+
        "as if it did not fit.  His tail was short and scraggly, and his "+
        "harness had been broken in many places and fastened together again "+
        "with cords and bits of wire.  The buggy seemed almost new, for it had "+
        "a shiny top and side curtains.  Getting around in front, so that she "+
        "could look inside, the girl saw a boy curled up on the seat, fast asleep.</p>",

        "<p>She set down the bird-cage and poked the boy with her parasol. "+
        "Presently he woke up, rose to a sitting position and rubbed "+
        "his eyes briskly.</p> "+
        "<p>&quot;Hello!&quot; he said, seeing her, &quot;are you Dorothy Gale?&quot;</p> "+
        "<p>&quot;Yes,&quot; she answered, looking gravely at his tousled hair and blinking "+
        "gray eyes.  &quot;Have you come to take me to Hugson&apos;s Ranch?&quot;</p> "+
        "<p>&quot;Of course,&quot; he answered.  &quot;Train in?&quot;</p> "+
        "<p>&quot;I couldn&apos;t be here if it wasn&apos;t,&quot; she said.</p>",

        "<p>He laughed at that, and his laugh was merry and frank.  Jumping out of "+
        "the buggy he put Dorothy&apos;s suit-case under the seat and her bird-cage "+
        "on the floor in front.</p> "+
        "<p>&quot;Canary-birds?&quot; he asked.</p> "+
        "<p>&quot;Oh no; it&apos;s just Eureka, my kitten.  I thought that was the best way "+
        "to carry her.&quot;</p> "+
        "<p>The boy nodded.</p> "+
        "<p>&quot;Eureka&apos;s a funny name for a cat,&quot; he remarked.</p> "+
        "<p>&quot;I named my kitten that because I found it,&quot; she explained.  &quot;Uncle "+
        "Henry says &apos;Eureka&apos; means &apos;I have found it.&apos;&quot;</p> "+
        "<p>&quot;All right; hop in.&quot;</p>",

        "<p>She climbed into the buggy and he followed her.  Then the boy picked "+
        "up the reins, shook them, and said &quot;Gid-dap!&quot;</p> "+
        "<p>The horse did not stir.  Dorothy thought he just wiggled one of his "+
        "drooping ears, but that was all.</p> "+
        "<p>&quot;Gid-dap!&quot; called the boy, again.</p> "+
        "<p>The horse stood still.</p> "+
        "<p>&quot;Perhaps,&quot; said Dorothy, &quot;if you untied him, he would go.&quot;</p> "+
        "<p>The boy laughed cheerfully and jumped out.</p> "+
        "<p>&quot;Guess I&apos;m half asleep yet,&quot; he said, untying the horse.  &quot;But Jim "+
        "knows his business all right - don&apos;t you, Jim?&quot; patting the long nose "+
        "of the animal.</p>",

        "<p>Then he got into the buggy again and took the reins, and the horse at "+
        "once backed away from the tree, turned slowly around, and began to "+
        "trot down the sandy road which was just visible in the dim light.</p> "+
        "<p>&quot;Thought that train would never come,&quot; observed the boy.  &quot;I&apos;ve "+
        "waited at that station for five hours.&quot;</p> "+
        "<p>&quot;We had a lot of earthquakes,&quot; said Dorothy.  &quot;Didn&apos;t you feel the "+
        "ground shake?&quot;</p> "+
        "<p>&quot;Yes; but we&apos;re used to such things in California,&quot; he replied.  &quot;They "+
        "don&apos;t scare us much.&quot;</p> "+
        "<p>&quot;The conductor said it was the worst quake he ever knew.&quot;</p> "+
        "<p>&quot;Did he?  Then it must have happened while I was asleep, "+
        "he said thoughtfully.</p>",

        "<p>&quot;How is Uncle Henry?&quot; she enquired, after a pause during which the "+
        "horse continued to trot with long, regular strides.</p> "+
        "<p>&quot;He&apos;s pretty well.  He and Uncle Hugson have been having a fine visit.&quot;</p> "+
        "<p>&quot;Is Mr. Hugson your uncle?&quot; she asked.</p> "+
        "<p>&quot;Yes.  Uncle Bill Hugson married your Uncle Henry&apos;s wife&apos;s sister; "+
        "so we must be second cousins,&quot; said the boy, in an amused tone. "+
        "&quot;I work for Uncle Bill on his ranch, and he pays me six dollars a month "+
        "and my board.&quot;</p> "+
        "<p>&quot;Isn&apos;t that a great deal?&quot; she asked, doubtfully.</p> "+
        "<p>&quot;Why, it&apos;s a great deal for Uncle Hugson, but not for me.  I&apos;m a "+
        "splendid worker.  I work as well as I sleep,&quot; he added, with a laugh.</p> ",
        
        "<p>&quot;What is your name?&quot; said Dorothy, thinking she liked the boy&apos;s manner "+
        "and the cheery tone of his voice.</p> "+
        "<p>&quot;Not a very pretty one,&quot; he answered, as if a little ashamed.  &quot;My "+
        "whole name is Zebediah; but folks just call me &apos;Zeb.&apos;  You&apos;ve been to "+
        "Australia, haven&apos;t you?&quot;</p> "+
        "<p>&quot;Yes; with Uncle Henry,&quot; she answered.  &quot;We got to San Francisco a "+
        "week ago, and Uncle Henry went right on to Hugson&apos;s Ranch for a visit "+
        "while I stayed a few days in the city with some friends we had met.&quot;</p> "+
        "<p>&quot;How long will you be with us?&quot; he asked.</p> "+
        "<p>&quot;Only a day.  Tomorrow Uncle Henry and I must start back for Kansas. "+
        "We&apos;ve been away for a long time, you know, and so we&apos;re anxious to get "+
        "home again.&quot;</p>",

        "<p>The boy flicked the big, boney horse with his whip and looked "+
        "thoughtful.  Then he started to say something to his little companion, "+
        "but before he could speak the buggy began to sway dangerously from side "+
        "to side and the earth seemed to rise up before them.  Next minute "+
        "there was a roar and a sharp crash, and at her side Dorothy saw the "+
        "ground open in a wide crack and then come together again.</p> "+
        "<p>&quot;Goodness!&quot; she cried, grasping the iron rail of the seat. "+
        "&quot;What was that?&quot;</p> "+
        "<p>&quot;That was an awful big quake,&quot; replied Zeb, with a white face.  &quot;It "+
        "almost got us that time, Dorothy.&quot;</p>",

        "<p>The horse had stopped short, and stood firm as a rock.  Zeb shook the "+
        "reins and urged him to go, but Jim was stubborn.  Then the boy cracked "+
        "his whip and touched the animal&apos;s flanks with it, and after a low moan "+
        "of protest Jim stepped slowly along the road.</p> "+
        "<p>Neither the boy nor the girl spoke again for some minutes.  There was "+
        "a breath of danger in the very air, and every few moments the earth "+
        "would shake violently.  Jim&apos;s ears were standing erect upon his head "+
        "and every muscle of his big body was tense as he trotted toward home. "+
        "He was not going very fast, but on his flanks specks of foam began to "+
        "appear and at times he would tremble like a leaf.</p>",

        "<p>The sky had grown darker again and the wind made queer sobbing sounds "+
        "as it swept over the valley.</p> "+
        "<p>Suddenly there was a rending, tearing sound, and the earth split into "+
        "another great crack just beneath the spot where the horse was "+
        "standing.  With a wild neigh of terror the animal fell bodily into the "+
        "pit, drawing the buggy and its occupants after him.</p> "+
        "<p>Dorothy grabbed fast hold of the buggy top and the boy did the same. "+
        "The sudden rush into space confused them so that they could not think.</p>",

        "<p>Blackness engulfed them on every side, and in breathless silence they "+
        "waited for the fall to end and crush them against jagged rocks or for "+
        "the earth to close in on them again and bury them forever in its "+
        "dreadful depths.</p> "+

        "<p>The horrible sensation of falling, the darkness and the terrifying "+
        "noises, proved more than Dorothy could endure and for a few moments "+
        "the little girl lost consciousness.  Zeb, being a boy, did not faint, "+
        "but he was badly frightened, and clung to the buggy seat with a tight "+
        "grip, expecting every moment would be his last.</p>",
    };
    
    // Javadoc inherited
    public String getPageContent(int number){
        return pages[number-1];
    }
    
    // Javadoc inherited
    public int getPagesCount() {
        return pages.length;
    }
}
