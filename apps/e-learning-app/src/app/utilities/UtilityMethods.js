//Contains utility methods

import { navMenuSize, linkSize, defaultNumber } from "../utilities/UtilityValues";

//Returns the top property for a sidebar based on its type, number of elements and position of the first element
export function calculateTop(type, size, pos) {
    if(type === 'grade') {
        return navMenuSize.toString() + 'vh';
    }
    if(defaultNumber - size < 0) {
        console.log("#1");
        return navMenuSize.toString() + 'vh';
    }
    if (size <= defaultNumber - pos) {
        console.log("#2");
        return (pos * linkSize + navMenuSize).toString() + 'vh';
    }
    console.log("#3");
    return ((defaultNumber - size) * linkSize + navMenuSize).toString() + 'vh';
}

//Return the bottom property for a sidebar based on its type, number of elements and position of the first element
export function calculateBottom(type, size, pos) {
    if (type === "grade") {
        return Math.max(100 - navMenuSize - size * linkSize, 0).toString() + 'vh';
    }
    if (size > defaultNumber) {
        return '0vh';
    }
    if (size <= defaultNumber - pos) {
        return (100 - navMenuSize - size * linkSize - pos * linkSize).toString() + 'vh';
    }
    return (100 - navMenuSize - defaultNumber * linkSize).toString() + 'vh';
}