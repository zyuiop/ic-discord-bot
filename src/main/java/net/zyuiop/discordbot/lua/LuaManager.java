package net.zyuiop.discordbot.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JseMathLib;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class LuaManager {
	private StringBuilder stringBuilder = new StringBuilder("```");
	private final IChannel channel;
	private String expectedInput = null;
	private boolean expectingInput = false;

	public LuaManager(IChannel channel) {this.channel = channel;}

	protected void flush() {
		try {
			channel.sendMessage(stringBuilder.append("```").toString());
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
		stringBuilder = new StringBuilder("```");
	}

	public void runScript(String script) {
		Globals user_globals = new Globals();
		user_globals.load(new LuaStandard(str -> {
			if (stringBuilder.length() + str.length() >= 1950) {
				flush();
			}
			stringBuilder.append(str);
		}));
		user_globals.load(new PackageLib());
		user_globals.load(new Bit32Lib());
		user_globals.load(new TableLib());
		user_globals.load(new StringLib());
		user_globals.load(new JseMathLib());


		LoadState.install(user_globals);
		LuaC.install(user_globals);

		// Starting coroutines in scripts will result in threads that are
		// not under the server control, so this libary should probably remain out.
		// user_globals.load(new CoroutineLib())

		// The debug library must be loaded for hook functions to work, which
		// allow us to limit scripts to run a certain number of instructions at a time.
		// However we don't wish to expose the library in the user globals,
		// so it is immediately removed from the user globals once created.
		user_globals.load(new DebugLib());
		LuaValue sethook = user_globals.get("debug").get("sethook");
		user_globals.set("debug", LuaValue.NIL);

		// Set up the script to run in its own lua thread, which allows us
		// to set a hook function that limits the script to a specific number of cycles.
		// Note that the environment is set to the user globals, even though the
		// compiling is done with the server globals.
		LuaValue chunk = user_globals.load(script, "main", user_globals);
		LuaThread thread = new LuaThread(user_globals, chunk);

		// Set the hook function to immediately throw an Error, which will not be
		// handled by any Lua code other than the coroutine.
		LuaValue hookfunc = new ZeroArgFunction() {
			private int calls = 0;

			public LuaValue call() {
				if (++calls >= 1000)
					throw new Error("Script overran resource limits (maximum amount of 1000 instructions bypassed).");
				return LuaValue.NIL;
			}
		};

		sethook.invoke(LuaValue.varargsOf(new LuaValue[]{thread, hookfunc,
				LuaValue.valueOf("l")}));

		// When we resume the thread, it will run up to 'instruction_count' instructions
		// then call the hook function which will error out and stop the script.
		Varargs result = thread.resume(LuaValue.NIL);
		try {
			flush();
			channel.sendMessage("Résultat final : `" + result.tojstring() + "`");
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}
}
