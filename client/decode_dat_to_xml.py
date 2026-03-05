#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
解码 .dat 文件到 XML 文件的脚本

使用方法:
    python decode_dat_to_xml.py <input_file> [--key KEY] [--output OUTPUT] [--batch]
    
示例:
    python decode_dat_to_xml.py ./xml/examproblems.dat  # 自动检测密钥
    python decode_dat_to_xml.py ./xml/user.dat  # 自动使用公钥
    python decode_dat_to_xml.py ./xml/1000000000/examproblems_26.dat  # 自动使用目录名作为密钥
    python decode_dat_to_xml.py ./xml --batch  # 批量处理整个目录
"""

import os
import sys
import argparse
from Crypto.Cipher import DES
from Crypto.Util.Padding import unpad

# 项目中的固定公钥（用于 user.dat 文件）
PUBLIC_KEY = "1413201160"

def detect_key_from_path(file_path):
    """
    从文件路径自动检测密钥
    
    Args:
        file_path: 文件路径
        
    Returns:
        检测到的密钥，如果无法检测则返回None
    """
    # 标准化路径
    normalized_path = os.path.normpath(file_path)
    path_parts = normalized_path.split(os.sep)
    
    # 检查是否是 user.dat 文件（使用公钥）
    if os.path.basename(normalized_path) == "user.dat":
        return PUBLIC_KEY
    
    # 检查文件是否在用户目录下（例如: xml/1000000000/examproblems_26.dat）
    # 用户目录通常是8-10位数字
    # 遍历路径的每一部分，查找数字目录名
    for i, part in enumerate(path_parts):
        # 检查是否是用户ID目录（8-10位数字）
        if part.isdigit() and 8 <= len(part) <= 10:
            # 如果这是目录名，且文件在这个目录下，使用这个目录名作为密钥
            # 例如: xml/1000000000/examproblems_26.dat
            if i < len(path_parts) - 1:  # 确保后面还有文件
                return part
    
    # 如果文件在 xml 根目录下，尝试从文件名或父目录推断
    # 但这种情况通常需要手动指定密钥
    return None

def decrypt_dat_file(input_file, key=None, output_file=None):
    """
    解密 .dat 文件到 XML
    
    Args:
        input_file: 输入的 .dat 文件路径
        key: DES 解密密钥（如果为None，则自动检测）
        output_file: 输出的 XML 文件路径（如果为None，则自动生成）
    """
    # 检查输入文件是否存在
    if not os.path.exists(input_file):
        print(f"错误: 文件不存在: {input_file}")
        return False
    
    # 如果没有提供密钥，尝试自动检测
    if key is None:
        detected_key = detect_key_from_path(input_file)
        if detected_key:
            key = detected_key
            print(f"自动检测到密钥: {key}")
        else:
            print(f"无法自动检测密钥，请手动指定")
            return False
    
    # 处理密钥：如果长度小于10，补0到10位（与Java代码逻辑一致）
    if len(key) < 10:
        key = key + "0" * (10 - len(key))
    
    # 确保密钥至少8位（DES要求）
    if len(key) < 8:
        key = key + "0" * (8 - len(key))
    
    # 只取前8位作为DES密钥（DES密钥必须是8字节）
    des_key = key[:8].encode('utf-8')
    
    # IV 固定为 "12345678"（与Java代码一致）
    iv = b"12345678"
    
    try:
        # 读取加密文件
        with open(input_file, 'rb') as f:
            encrypted_data = f.read()
        
        if len(encrypted_data) == 0:
            print("错误: 文件为空")
            return False
        
        # 创建 DES 解密器
        cipher = DES.new(des_key, DES.MODE_CBC, iv)
        
        # 解密数据
        decrypted_data = cipher.decrypt(encrypted_data)
        
        # 去除 PKCS5 填充
        try:
            decrypted_data = unpad(decrypted_data, 8)
        except ValueError as e:
            print(f"警告: 去除填充时出错，可能文件格式不正确: {e}")
            # 尝试直接使用，可能是未填充的数据
            pass
        
        # 尝试解码为字符串（使用GBK编码，因为Java代码中使用了GBK）
        try:
            xml_content = decrypted_data.decode('GBK')
        except UnicodeDecodeError:
            # 如果GBK失败，尝试UTF-8
            try:
                xml_content = decrypted_data.decode('UTF-8')
            except UnicodeDecodeError:
                # 如果都失败，尝试使用错误处理
                xml_content = decrypted_data.decode('GBK', errors='ignore')
                print("警告: 使用GBK解码时遇到无法解码的字符，已忽略")
        
        # 确定输出文件路径
        if output_file is None:
            # 自动生成输出文件名：将 .dat 扩展名改为 .xml
            base_name = os.path.splitext(input_file)[0]
            output_file = base_name + ".xml"
        
        # 写入XML文件
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(xml_content)
        
        print(f"成功! 解密后的XML文件已保存到: {output_file}")
        return True
        
    except Exception as e:
        print(f"错误: 解密过程中出现异常: {e}")
        import traceback
        traceback.print_exc()
        return False

def batch_process(directory):
    """
    批量处理目录中的所有 .dat 文件
    
    Args:
        directory: 要处理的目录路径
    """
    if not os.path.isdir(directory):
        print(f"错误: {directory} 不是一个有效的目录")
        return False
    
    success_count = 0
    fail_count = 0
    
    # 递归查找所有 .dat 文件
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.dat'):
                file_path = os.path.join(root, file)
                print(f"\n处理文件: {file_path}")
                if decrypt_dat_file(file_path):
                    success_count += 1
                else:
                    fail_count += 1
    
    print(f"\n批量处理完成: 成功 {success_count} 个, 失败 {fail_count} 个")
    return fail_count == 0

def main():
    parser = argparse.ArgumentParser(
        description='解码 .dat 文件到 XML 文件（自动检测密钥）',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
示例:
  %(prog)s ./xml/examproblems.dat  # 自动检测密钥
  %(prog)s ./xml/user.dat  # 自动使用公钥 "1413201160"
  %(prog)s ./xml/1000000000/examproblems_26.dat  # 自动使用目录名作为密钥
  %(prog)s ./xml/examproblems.dat --key "userid1234"  # 手动指定密钥
  %(prog)s ./xml --batch  # 批量处理整个目录
        """
    )
    
    parser.add_argument('input_path', help='输入的 .dat 文件路径或目录路径（使用 --batch 时）')
    parser.add_argument('--key', '-k', help='DES 解密密钥（用户ID，至少8位）。如果不指定，将自动检测')
    parser.add_argument('--output', '-o', help='输出的 XML 文件路径（默认为输入文件名.xml）')
    parser.add_argument('--batch', '-b', action='store_true', help='批量处理目录中的所有 .dat 文件')
    
    args = parser.parse_args()
    
    # 批量处理模式
    if args.batch:
        success = batch_process(args.input_path)
        sys.exit(0 if success else 1)
    
    # 单个文件处理模式
    # 如果没有提供密钥，尝试自动检测，如果检测失败则提示用户输入
    key = args.key
    if key is None:
        detected_key = detect_key_from_path(args.input_path)
        if detected_key:
            key = detected_key
            print(f"自动检测到密钥: {key}")
        else:
            key = input("无法自动检测密钥，请输入解密密钥（用户ID）: ").strip()
            if not key:
                print("错误: 密钥不能为空")
                sys.exit(1)
    
    # 执行解密
    success = decrypt_dat_file(args.input_path, key, args.output)
    
    if not success:
        sys.exit(1)

if __name__ == '__main__':
    main()

