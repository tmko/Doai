package poc.doai;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Application {

    public static void main (String[] args) {


        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext();

            context.tracing().start(
                    new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false)
            );

            Page page = context.newPage();
            page.navigate("http://playwright.dev");

            // Expect a title "to contain" a substring.
            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            // create a locator
            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            // Expect an attribute "to be strictly equal" to the value.
            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            // Click the get started link.
            getStarted.click();

            // Expects the URL to contain intro.
            assertThat(page).hasURL(Pattern.compile(".*intro"));

            context.tracing().stop(
                    new Tracing
                            .StopOptions()
                            .setPath(Paths.get("../target/trace.zip"))
                    );

            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("../target/example.png")));
        }
    }
}
