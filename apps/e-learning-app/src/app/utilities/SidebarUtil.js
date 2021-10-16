//Contains utility methods and constants values used in other files
import {
  navBarHeight,
  sidebarButtonHeight,
} from '../themes/Sizes';

const defaultNumber = Math.floor((100 - navBarHeight) / sidebarButtonHeight);

//Returns the top property for a sidebar based on its type, number of elements and position of the first element
export function calculateTop(type, size, pos) {
  if (type === 'grade') {
    return navBarHeight.toString() + 'vh';
  }
  if (defaultNumber - size < 0) {
    console.log('#1');
    return navBarHeight.toString() + 'vh';
  }
  if (size <= defaultNumber - pos) {
    console.log('#2');
    return (pos * sidebarButtonHeight + navBarHeight).toString() + 'vh';
  }
  console.log('#3');
  return ((defaultNumber - size) * sidebarButtonHeight + navBarHeight).toString() + 'vh';
}

//Return the bottom property for a sidebar based on its type, number of elements and position of the first element
export function calculateBottom(type, size, pos) {
  if (type === 'grade') {
    return Math.max(100 - navBarHeight - size * sidebarButtonHeight, 0).toString() + 'vh';
  }
  if (size > defaultNumber) {
    return '0vh';
  }
  if (size <= defaultNumber - pos) {
    return (
      (100 - navBarHeight - size * sidebarButtonHeight - pos * sidebarButtonHeight).toString() + 'vh'
    );
  }
  return (100 - navBarHeight - defaultNumber * sidebarButtonHeight).toString() + 'vh';
}
