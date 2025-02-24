package com.example.mod;

//#if FABRIC
//$$ import net.fabricmc.api.ClientModInitializer;
//#else
import net.minecraftforge.fml.common.Mod;
//#endif

//#if FORGE
//#if MC>=11600
//$$ @Mod("examplemod")
//#else
@Mod(modid = "examplemod")
//#endif
//#endif
public class ExampleMod
    //#if FABRIC
    //$$ implements ClientModInitializer
    //#endif
{
    //#if FABRIC
    //$$ @Override
    //$$ public void onInitializeClient() {
    //#else
    {
    //#endif
        System.out.println("Hello World from Example Mod!");
    }

    //#if MC<11600
    public static void transformerHello() {
        System.out.println("Hello World from Example Mod Class Transformer!");
    }
    //#endif
}
