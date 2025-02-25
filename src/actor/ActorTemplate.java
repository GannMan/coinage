package actor;

import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import game.Game;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * A stored Prototype from which prefab Actors can be produced. Currently uses a hard-coded
 * static set for its library, will eventually use raw text resources.
 */
public class ActorTemplate {

  final String name;
  final Appearance appearance;

  final EnumSet<PhysicalFlag> flags;
  final List<AttributeRange> baseAttributeRanges;

  public ActorTemplate(String name, char appearance, Color color, Color bgColor,
                       List<AttributeRange> baseAttributeRanges) {
    this.name = name;
    this.appearance = new Appearance(appearance,color,bgColor, Game.VISUAL_PRIORITY__ACTORS);
    this.baseAttributeRanges = baseAttributeRanges;
    flags = EnumSet.noneOf(PhysicalFlag.class);
  }

  public static HashMap<String, ActorTemplate> LIB = new HashMap<>();
  static {

    LIB.put("HUMAN", new ActorTemplate(

        "a human", 'H', new Color(129, 84, 51), new Color(40, 26, 16),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),        // MUSCLE
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),        // GRIT
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),        // REFLEX
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),        // TALENT
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),        // PERCEPTION
          AttributeRange.fromRank(Rank.R05_AVERAGE,1)         // CHARM
        )

    ));

    LIB.put("DOG", new ActorTemplate(

        "a dog", 'd', new Color(139, 47, 16), new Color(32, 11, 4),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R03_INFERIOR,2),                   // MUSCLE
          AttributeRange.fromRank(Rank.R03_INFERIOR,2),                   // GRIT
          AttributeRange.fromRank(Rank.R06_ABOVE_AVERAGE,1),              // REFLEX
          new AttributeRange(Rank.R02_TERRIBLE,Rank.R03_INFERIOR),        // TALENT
          new AttributeRange(Rank.R09_EXCEPTIONAL,Rank.R10_MASTERFUL),    // PERCEPTION
          AttributeRange.fromRank(Rank.R05_AVERAGE,1)                     // CHARM
        )

    ));

    LIB.put("CAT", new ActorTemplate(

        "a cat", 'c', new Color(130, 2, 0), new Color(37, 1, 0),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R02_TERRIBLE,1),                   // MUSCLE
          new AttributeRange(Rank.R01_ABYSMAL,Rank.R02_TERRIBLE),         // GRIT
          new AttributeRange(Rank.R08_OUTSTANDING,Rank.R09_EXCEPTIONAL),  // REFLEX
          new AttributeRange(Rank.R02_TERRIBLE,Rank.R03_INFERIOR),        // TALENT
          new AttributeRange(Rank.R09_EXCEPTIONAL,Rank.R10_MASTERFUL),    // PERCEPTION
          AttributeRange.fromRank(Rank.R05_AVERAGE,1)                     // CHARM
        )

    ));

    LIB.put("MOUSE", new ActorTemplate(

        "a mouse", 'm', new Color(0, 85, 130), new Color(0, 24, 37),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // MUSCLE
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // GRIT
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // REFLEX
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // TALENT
          AttributeRange.fromRank(Rank.R02_TERRIBLE,0),                   // PERCEPTION
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0)                     // CHARM
        )

    ));

    LIB.put("GIBBERLING", new ActorTemplate(

        "a foul gibberling", 'g',
        new Color(43, 130, 78),
        new Color(41, 81, 30),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),                    // MUSCLE
          AttributeRange.fromRank(Rank.R03_INFERIOR,2),                   // GRIT
          AttributeRange.fromRank(Rank.R03_INFERIOR,2),                   // REFLEX
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // TALENT
          AttributeRange.fromRank(Rank.R03_INFERIOR,0),                   // PERCEPTION
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0)                     // CHARM
        )

    ));

  }

}
