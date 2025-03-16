package dev.boze.client.systems.modules.render;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class ViewModel extends Module {
   public static final ViewModel INSTANCE = new ViewModel();
   public final BooleanSetting field3838 = new BooleanSetting("Same", false, "Apply the same transformations to both hands");
   public final FloatSetting field3839 = new FloatSetting("OffX", 0.0F, -2.0F, 2.0F, 0.01F, "X Offset for both hands", this.field3838::method419);
   public final FloatSetting field3840 = new FloatSetting("OffY", 0.0F, -2.0F, 2.0F, 0.01F, "Y Offset for both hands", this.field3838::method419);
   public final FloatSetting field3841 = new FloatSetting("OffZ", 0.0F, -2.0F, 2.0F, 0.01F, "Z Offset for both hands", this.field3838::method419);
   public final FloatSetting field3842 = new FloatSetting("RotX", 0.0F, -180.0F, 180.0F, 1.0F, "X Rotation for both hands", this.field3838::method419);
   public final FloatSetting field3843 = new FloatSetting("RotY", 0.0F, -180.0F, 180.0F, 1.0F, "Y Rotation for both hands", this.field3838::method419);
   public final FloatSetting field3844 = new FloatSetting("RotZ", 0.0F, -180.0F, 180.0F, 1.0F, "Z Rotation for both hands", this.field3838::method419);
   public final FloatSetting field3845 = new FloatSetting("ScaleX", 1.0F, 0.0F, 2.0F, 0.01F, "X Scale for both hands", this.field3838::method419);
   public final FloatSetting field3846 = new FloatSetting("ScaleY", 1.0F, 0.0F, 2.0F, 0.01F, "Y Scale for both hands", this.field3838::method419);
   public final FloatSetting field3847 = new FloatSetting("ScaleZ", 1.0F, 0.0F, 2.0F, 0.01F, "Z Scale for both hands", this.field3838::method419);
   public final BooleanSetting field3848 = new BooleanSetting("MainHand", true, "Transformations for main hand", this::lambda$new$0);
   public final FloatSetting field3849 = new FloatSetting("OffX", 0.0F, -2.0F, 2.0F, 0.01F, "X Offset for main hand", this.field3848);
   public final FloatSetting field3850 = new FloatSetting("OffY", 0.0F, -2.0F, 2.0F, 0.01F, "Y Offset for main hand", this.field3848);
   public final FloatSetting field3851 = new FloatSetting("OffZ", 0.0F, -2.0F, 2.0F, 0.01F, "Z Offset for main hand", this.field3848);
   public final FloatSetting field3852 = new FloatSetting("RotX", 0.0F, -180.0F, 180.0F, 1.0F, "X Rotation for main hand", this.field3848);
   public final FloatSetting field3853 = new FloatSetting("RotY", 0.0F, -180.0F, 180.0F, 1.0F, "Y Rotation for main hand", this.field3848);
   public final FloatSetting field3854 = new FloatSetting("RotZ", 0.0F, -180.0F, 180.0F, 1.0F, "Z Rotation for main hand", this.field3848);
   public final FloatSetting field3855 = new FloatSetting("ScaleX", 1.0F, 0.0F, 2.0F, 0.01F, "X Scale for main hand", this.field3848);
   public final FloatSetting field3856 = new FloatSetting("ScaleY", 1.0F, 0.0F, 2.0F, 0.01F, "Y Scale for main hand", this.field3848);
   public final FloatSetting field3857 = new FloatSetting("ScaleZ", 1.0F, 0.0F, 2.0F, 0.01F, "Z Scale for main hand", this.field3848);
   public final BooleanSetting field3858 = new BooleanSetting("OffHand", true, "Transformations for off hand", this::lambda$new$1);
   public final FloatSetting field3859 = new FloatSetting("OffX", 0.0F, -2.0F, 2.0F, 0.01F, "X Offset for off hand", this.field3858);
   public final FloatSetting field3860 = new FloatSetting("OffY", 0.0F, -2.0F, 2.0F, 0.01F, "Y Offset for off hand", this.field3858);
   public final FloatSetting field3861 = new FloatSetting("OffZ", 0.0F, -2.0F, 2.0F, 0.01F, "Z Offset for off hand", this.field3858);
   public final FloatSetting field3862 = new FloatSetting("RotX", 0.0F, -180.0F, 180.0F, 1.0F, "X Rotation for off hand", this.field3858);
   public final FloatSetting field3863 = new FloatSetting("RotY", 0.0F, -180.0F, 180.0F, 1.0F, "Y Rotation for off hand", this.field3858);
   public final FloatSetting field3864 = new FloatSetting("RotZ", 0.0F, -180.0F, 180.0F, 1.0F, "Z Rotation for off hand", this.field3858);
   public final FloatSetting field3865 = new FloatSetting("ScaleX", 1.0F, 0.0F, 2.0F, 0.01F, "X Scale for off hand", this.field3858);
   public final FloatSetting field3866 = new FloatSetting("ScaleY", 1.0F, 0.0F, 2.0F, 0.01F, "Y Scale for off hand", this.field3858);
   public final FloatSetting aa = new FloatSetting("ScaleZ", 1.0F, 0.0F, 2.0F, 0.01F, "Z Scale for off hand", this.field3858);

   public ViewModel() {
      super("ViewModel", "Transforms held items", Category.Render);
   }

   private boolean lambda$new$1() {
      return !this.field3838.method419();
   }

   private boolean lambda$new$0() {
      return !this.field3838.method419();
   }
}
