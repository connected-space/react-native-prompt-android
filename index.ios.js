import {
    AlertIOS
} from 'react-native';

export default function prompt(
    title: ?string,
    message?: ?string,
    callbackOrButtons?: ?((text: string) => void) | Object,
    options?: ?Object
): void {
    const type = options && options.type ? options.type : null;
    const defaultValue = options && options.defaultValue
        ? options.defaultValue
        : null;
    AlertIOS.prompt(title, message, callbackOrButtons, type, defaultValue);
}
