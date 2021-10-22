//Contains utility methods and constants values used in other files
import { navBarHeight, sidebarButtonHeight, minNavBarHeight, minSidebarButtonHeight } from '../themes/Sizes';

let maxSidebarButtonsAmount;
let currentNavBarHeight;
let currentSidebarButtonHeight;
let unit;
let currentWindowHeight;

/**
 * Calculates the top offset that is applied to the sidebar, based on the given parameters.
 * @param {String} type - The type of the Sidebar: Grade/Subject.
 * @param {Number} amount - Amount of buttons on sidebar.
 * @param {Number} index - Position of the first element of the sidebar.
 * @param {Number} windowHeight - Current height of the viewport. 
 * @returns {String} top offset, calculated in vh.
 */
export function calculateTop(type, amount, index, windowHeight) {

  if (navBarHeight * windowHeight / 100 >= minNavBarHeight) {
    // Sizes scale with the viewport
    currentNavBarHeight = navBarHeight;
    currentSidebarButtonHeight = sidebarButtonHeight;
    currentWindowHeight = 100;
    unit = "vh";
  } else {
    // Sizes are fixed
    currentNavBarHeight = minNavBarHeight;
    currentSidebarButtonHeight = minSidebarButtonHeight;
    currentWindowHeight = windowHeight;
    unit = "px";
  }
  maxSidebarButtonsAmount = Math.floor(
    (currentWindowHeight - currentNavBarHeight) / currentSidebarButtonHeight
  );

  // First Sidebar
  if (type === 'grade') {
    return currentNavBarHeight.toString() + unit;
  }

  // Second Sidebar
  if (maxSidebarButtonsAmount < amount) {
    // If there are more elements than the maximum allowed.
    return currentNavBarHeight.toString() + unit;
  }

  if (amount <= maxSidebarButtonsAmount - index) {
    // If the elements can fit from the corresponding index
    return (index * currentSidebarButtonHeight + currentNavBarHeight).toString() + unit;
  }

  // If the elements cannot fit from the corresponding index
  return (
    (
      (maxSidebarButtonsAmount - amount) * currentSidebarButtonHeight +
      currentNavBarHeight
    ).toString() + unit
  );
}

/**
 * Calculates the bottom offset that is applied to the sidebar, based on the given parameters.
 * @param {String} type - The type of the Sidebar: Grade/Subject.
 * @param {Number} amount - Amount of buttons on sidebar.
 * @param {Number} index - Position of the first element of the sidebar.
 * @param {Number} windowHeight - Current height of the viewport. 
 * @returns {String} bottom offset, calculated in vh.
 */
export function calculateBottom(type, amount, index, windowHeight) {
  if (navBarHeight * windowHeight / 100 >= minNavBarHeight) {
    // Sizes scale with the viewport
    currentNavBarHeight = navBarHeight;
    currentSidebarButtonHeight = sidebarButtonHeight;
    currentWindowHeight = 100;
    unit = "vh";
  } else {
    // Sizes are fixed
    currentNavBarHeight = minNavBarHeight;
    currentSidebarButtonHeight = minSidebarButtonHeight;
    currentWindowHeight = windowHeight;
    unit = "px";
  }
  maxSidebarButtonsAmount = Math.floor(
    (currentWindowHeight - currentNavBarHeight) / currentSidebarButtonHeight
  );

  // First Sidebar
  if (type === 'grade') {
    return (
      Math.max(
        currentWindowHeight - currentNavBarHeight - amount * currentSidebarButtonHeight,
        0
      ).toString() + unit
    );
  }
  // Second sidebar
  if (maxSidebarButtonsAmount < amount) {
    // If there are more elements than the maximum allowed.
    return '0' + unit;
  }

  if (amount <= maxSidebarButtonsAmount - index) {
    // If the elements can fit from the corresponding index
    return (
      (
        currentWindowHeight -
        currentNavBarHeight -
        amount * currentSidebarButtonHeight -
        index * currentSidebarButtonHeight
      ).toString() + unit
    );
  }

  // If the elements cannot fit from the corresponding index
  return (
    (
      currentWindowHeight -
      currentNavBarHeight -
      maxSidebarButtonsAmount * currentSidebarButtonHeight
    ).toString() + unit
  );
}
