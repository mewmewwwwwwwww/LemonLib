package onelemonyboi.lemonlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import onelemonyboi.lemonlib.rewards.PatreonRewards;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(LemonLib.MOD_ID)
public class LemonLib {
    public static final String MOD_ID = "lemonlib";
    public static final Logger LOGGER = LogManager.getLogger();

    public LemonLib()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        EVENT_BUS.addListener(PatreonRewards::PatreonRewardsHandling);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        this.preInit(event);
        this.init(event);
        this.postInit(event);
    }
    private void doClientStuff(final FMLClientSetupEvent event) // Render Stuff HERE!!
    {
    }

    private void enqueueIMC(InterModEnqueueEvent event) {

    }

    private void preInit(FMLCommonSetupEvent event) {

    }

    private void init(FMLCommonSetupEvent event) {

    }

    private void postInit(FMLCommonSetupEvent event) {

    }

    public static Logger getLogger()
    {
        return LOGGER;
    }
}
