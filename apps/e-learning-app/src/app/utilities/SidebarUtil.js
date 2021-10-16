//Contains utility methods and constants values used in other files
import { navBarHeight, sidebarButtonHeight } from '../themes/Sizes';

const maxSidebarButtonsAmount = Math.floor(
  (100 - navBarHeight) / sidebarButtonHeight
);

/**
 * Calculates the top offset that is applied to the sidebar, based on the given parameters.
 * @param {String} type - The type of the Sidebar: Grade/Subject.
 * @param {Number} amount - Amount of buttons on sidebar.
 * @param {Number} index - Position of the first element of the sidebar.
 * @returns {String} top offset, calculated in vh.
 */
export function calculateTop(type, amount, index) {
  // First Sidebar
  if (type === 'grade') {
    return navBarHeight.toString() + 'vh';
  }
  // Second Sidebar
  if (maxSidebarButtonsAmount < amount) {
    // If there are more elements than the maximum allowed.
    return navBarHeight.toString() + 'vh';
  }
  if (amount <= maxSidebarButtonsAmount - index) {
    // If the elements can fit from the corresponding index
    return (index * sidebarButtonHeight + navBarHeight).toString() + 'vh';
  }

  // If the elements cannot fit from the corresponding index
  return (
    (
      (maxSidebarButtonsAmount - amount) * sidebarButtonHeight +
      navBarHeight
    ).toString() + 'vh'
  );
}

//Return the bottom property for a sidebar based on its type, number of elements and position of the first element
export function calculateBottom(type, amount, index) {
  if (type === 'grade') {
    return (
      Math.max(
        100 - navBarHeight - amount * sidebarButtonHeight,
        0
      ).toString() + 'vh'
    );
  }
  if (amount > maxSidebarButtonsAmount) {
    return '0vh';
  }
  if (amount <= maxSidebarButtonsAmount - index) {
    return (
      (
        100 -
        navBarHeight -
        amount * sidebarButtonHeight -
        index * sidebarButtonHeight
      ).toString() + 'vh'
    );
  }
  return (
    (
      100 -
      navBarHeight -
      maxSidebarButtonsAmount * sidebarButtonHeight
    ).toString() + 'vh'
  );
}
