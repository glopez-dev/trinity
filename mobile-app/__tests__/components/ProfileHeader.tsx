import {render} from "@testing-library/react-native";
import ProfileHeader from "@/components/feature/profile/header/ProfileHeader";

describe('<ProfileHeader />', () => {
    test('should render ProfileHeader', () => {
        const {getByText, getByA11yHint} = render(<ProfileHeader name={'John Doe'} email={'john.doe@gmail.com'} />);
        expect(getByText('John Doe')).toBeTruthy();
        expect(getByText('john.doe@gmail.com')).toBeTruthy();
        expect(getByA11yHint('header')).toBeTruthy();
        expect(getByA11yHint('icon-profil')).toBeTruthy();
        expect(getByA11yHint('name-email')).toBeTruthy();
        expect(getByA11yHint('edit')).toBeTruthy();
    })
});