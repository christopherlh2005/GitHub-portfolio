// Testing class
import { expect, test } from "@playwright/test";
import { setupClerkTestingToken, clerk } from "@clerk/testing/playwright";

/**
 * Intializes connection to local host before each test
 */

test.beforeEach('testing defaults before each', async ({ page }) => {
  await setupClerkTestingToken({ page })
  await page.goto('http://localhost:8000/');

  await clerk.loaded({ page });
  const loginButton = page.getByRole("button", {name : "Sign in"})
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    }
  })
});

/**
 * Check that tabular values display as expected
 */
test('test', async ({ page }) => {

  await expect(page.getByLabel('Welcome Message')).toBeVisible();
  await expect(page.getByLabel('Instructions Paragraph')).toBeVisible();
  await page.getByLabel('dropdown').selectOption('washington_counties.csv');
  await expect(page.getByLabel('row King County, column State')).toContainText('King County');
  await page.getByLabel('row King County, column Broadband_Percentage, value').click();
  await page.getByLabel('row Pierce County, column State-County').click();
  await page.getByLabel('row Pierce County, column Broadband_Percentage, value').click();
  await page.getByLabel('row Snohomish County, column State-County').click();
  await page.getByLabel('row Snohomish County, column Broadband_Percentage, value').click();
  await page.getByLabel('Switch to Graph').click();
  await expect(page.getByLabel('Bar Chart')).toBeVisible();
});


/**
 * Check invalid search queries and that it shows the appropriate message
 */
test('no_match_test', async ({ page }) => {
  await page.getByRole('textbox', { name: 'Enter What You Want to Search' }).fill('imaginary county');
  await page.getByRole('button', { name: 'Search Census API' }).click();
  await page.getByText('No matches found.').click();
});


/**
 * Check invalid data for bar graphs and that it shows the appropriate message
 */
test('error_test', async ({ page }) => {
  await page.getByLabel('dropdown').selectOption('texas_counties.csv');
  await page.getByRole('button', { name: 'Switch to Graph' }).click();
  await expect(page.getByLabel('error-message')).toBeVisible();
  await expect(page.getByLabel('error-message')).toContainText('Error: Too many rows or columns for graph view (max 10 each).');
});

/**
 * Test that broadband API calls work
 */
test('api_call_test', async ({ page }) => {
  await page.getByRole('textbox', { name: 'Enter What You Want to Search' }).fill('New York County, New York');
  await page.getByRole('textbox', { name: 'Enter What You Want to Search' }).click();
  await page.getByRole('button', { name: 'Search Census API' }).click();
  await page.getByRole('button', { name: 'Search Census API' }).click();
  await page.getByText('Broadband Coverage: 83.9%').click();
});


/**
 * Test behavior on headerless data
 */
test('header_headerless_test', async ({ page }) => {
  await page.getByLabel('dropdown').selectOption('headerless.csv');
  await expect(page.getByLabel('error-message')).toContainText('Error: Missing header in CSV.');
  await page.getByLabel('dropdown').selectOption('texas_counties.csv');
  await expect(page.getByLabel('row Harris County, column State-County')).toContainText('Harris County');
  await expect(page.getByLabel('row Harris County, column Broadband_Percentage, value')).toContainText('89.3');

});