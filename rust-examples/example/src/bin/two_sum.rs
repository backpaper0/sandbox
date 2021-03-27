fn main() {
    assert_eq!(vec![0, 1], Solution::two_sum(vec![2, 7, 11, 15], 9));
    assert_eq!(vec![1, 2], Solution::two_sum(vec![3, 2, 4], 6));
    assert_eq!(vec![0, 1], Solution::two_sum(vec![3, 3], 6));
    println!("OK");
}

struct Solution;

impl Solution {
    pub fn two_sum(nums: Vec<i32>, target: i32) -> Vec<i32> {
        for (a, i) in nums.iter().zip(0..) {
            for (b, j) in nums.iter().zip(0..) {
                if i < j && a + b == target {
                    return vec![i, j];
                }
            }
        }
        panic!("error");
    }
}

