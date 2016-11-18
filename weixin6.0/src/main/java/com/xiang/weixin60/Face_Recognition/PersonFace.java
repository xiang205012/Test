package com.xiang.weixin60.Face_Recognition;

import java.util.List;

/**
 * Created by gordon on 2016/8/6.
 */
public class PersonFace {

    public int img_width;
    public int img_height;
    public int img_id;
    public int session_id;
    public String url;

    public List<Face> face;

    public class Face{

        public int face_id;
        public String tag;
        public Attribute attribute;
        public Position position;

        public class Attribute{
            public Age age;
            public Gender gender;
            public Glass glass;
            public Pose pose;
            public Race race;
            public Smiling smiling;

            public class Age{
                public int range;
                public int value;
            }
            public class Gender{
                public double confidence;
                public String value; // 性别 Female(女) Male(男)
            }
            public class Glass{
                public double confidence;
                public String value;
            }
            public class Pose{
                public PitchAngle pitch_angle;
                public RollAngle roll_angle;
                public YawAngle yaw_angle;
                public class PitchAngle{
                    public double value;
                }
                public class RollAngle{
                    public double value;
                }
                public class YawAngle{
                    public double value;
                }
            }
            public class Race{
                public double confidence;
                public String value;
            }
            public class Smiling{
                public double value;
            }
        }

        // 检测后的矩形中 各个值
        public class Position{
            public double width;
            public double height;
            public Center center;
            public EyeLeft eye_left;
            public EyeRight eye_right;
            public MouthLeft mouth_left;
            public MouthRight mouth_right;
            public Nose nose;

            public class Center{
                public double x;// 中心点的位置在检测后的矩形中 x方向是 x%的位置，百分比的形式
                public double y;// 同理
            }
            public class EyeLeft{
                public double x;// 中心点的位置在检测后的矩形中 x方向是 x%的位置，百分比的形式
                public double y;// 同理
            }
            public class EyeRight{
                public double x;
                public double y;
            }
            public class MouthLeft{
                public double x;
                public double y;
            }
            public class MouthRight{
                public double x;
                public double y;
            }
            public class Nose{
                public double x;
                public double y;
            }
        }


    }

}
